package com.be.byeoldam.user;

import com.be.byeoldam.common.filter.jwt.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JwtUtilTest {
    private JWTUtil jwtUtil;
    private String token;
    private final long userId = 1L;
    private final String email = "test@example.com";
    private final String role = "USER";
    private final long expirationMs = 60000L; // 1분

    @BeforeEach
    void setUp() {
        jwtUtil = new JWTUtil();
        token = jwtUtil.createJwt(userId, email, role, expirationMs);
    }

    @Test
    @DisplayName("JWT 기능 테스트")
    void testJwt(){
        Long extractedUserId = jwtUtil.getUserId(token);
        String extractedEmail = jwtUtil.getUserEmail(token);
        String extractedRole = jwtUtil.getRole(token);

        assertEquals(userId, extractedUserId);
        assertEquals(email, extractedEmail);
        assertEquals(role, extractedRole);
    }
}
