package com.be.byeoldam.common.filter;

import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("-----CustomUserDetailService.loadUserByUsername-----");

        User userData = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("데이터 없음");
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        return new CustomUserDetails(userData);
    }
}
