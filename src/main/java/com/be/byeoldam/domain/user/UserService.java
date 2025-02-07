package com.be.byeoldam.domain.user;

import com.be.byeoldam.common.jwt.JwtUtil;
import com.be.byeoldam.domain.user.dto.*;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 이메일 인증번호 발송(이메일 중복 확인 + 유효 코드 보내기)

    // 인증코드 확인

    // 회원 가입
    @Transactional
    UserRegisterResponse registerUser(UserRegisterRequest registerRequest) {
        User user = registerRequest.toEntity();
        user.encodePassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        Map<String, String> tokens = generateTokens(user);
        user.updateRefreshToken(tokens.get("refresh"));
        userRepository.save(user);

        UserRegisterResponse userRegisterResponse = UserRegisterResponse.fromEntity(user);
        userRegisterResponse.addTokens(tokens.get("access"), tokens.get("refresh"));

        return userRegisterResponse;
    }

    // 로그인
    @Transactional
    UserLoginResponse login(UserLoginRequest userLoginRequest){
        User user = userRepository.findByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new CustomException("이메일을 다시 한번 확인해주세요."));

        if(!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())){
            throw new CustomException("비밀번호가 일치하지 않습니다.");
        }

        Map<String, String> tokens = generateTokens(user);
        user.updateRefreshToken(tokens.get("refresh"));
        userRepository.save(user);

        UserLoginResponse userLoginResponse = UserLoginResponse.fromEntity(user);
        userLoginResponse.addTokens(tokens.get("access"), tokens.get("refresh"));

        return userLoginResponse;
    }

    // 로그아웃
    @Transactional
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException("로그인된 사용자가 없습니다.");
        }

        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("유저를 찾을 수 없습니다."));

        user.updateRefreshToken(null);
        userRepository.save(user);
    }

    // refreshToken을 통해 access토큰 재발급
    @Transactional
    public UserTokenResponse refreshToken(String refreshToken) {
        Long userId = jwtUtil.getUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("올바르지 않은 RefreshToken입니다."));

        if(!user.getRefreshToken().equals(refreshToken)){
            throw new CustomException("올바르지 않은 RefreshToken입니다.");
        }

        Map<String, String> tokens = generateTokens(user);
        user.updateRefreshToken(tokens.get("refresh"));

        return UserTokenResponse.of(tokens.get("access"), tokens.get("refresh"));
    }

    // JWT필터에서 refreshToken 업데이트 하기
    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }

    private Map<String, String> generateTokens(User user) {
        String accessToken = jwtUtil.createJwt("access", user.getId(), user.getEmail(), user.getRole(), 6000000L); // 10시간(추후개선)
        String refreshToken = jwtUtil.createJwt("refresh", user.getId(), user.getEmail(), user.getRole(), 86400000L); //24시간

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", accessToken);
        tokens.put("refresh", refreshToken);
        return tokens;
    }
}
