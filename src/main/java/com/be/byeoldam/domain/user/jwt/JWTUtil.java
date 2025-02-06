package com.be.byeoldam.domain.user.jwt;


import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private String secret = "vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdb alaaaaaaaaaaaaaaaabbbbb";
    private SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());

    //jwt 생성
    public String createJwt(long userId, String email, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("userId",userId)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) //발급 시간 설정
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) //토큰 만료시간 설정
                .signWith(secretKey) // JWT 서명을 위해 비밀 키를 지정
                .compact(); //compact: JWT를 문자열 형태로 직렬화
    }

    public Long getUserId(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
    }

    public String getUserEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

}
