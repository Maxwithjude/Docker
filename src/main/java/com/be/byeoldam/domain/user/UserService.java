package com.be.byeoldam.domain.user;

import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    // 이메일 인증번호 발송(이메일 중복 확인 + 유효 코드 보내기)

    // 인증코드 확인

    //회원 가입
    @Transactional
    void registerUser(UserRegisterRequest registerRequest) {
        User user = registerRequest.toEntity();
        userRepository.save(user);
    }
}
