package com.be.byeoldam.common.filter;

import com.be.byeoldam.common.jwt.JwtUtil;
import com.be.byeoldam.domain.user.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JWTFilter.doFilterInternal");

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");

        // 1. 토큰이 없다면 다음 필터(로그인 필터)로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            //response body & 응답코드
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 refreshToken 일 경우
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            //refreshToken 로직 작성하기.
            return;
        }

        // access 토큰이 있는 경우
        if (accessToken != null) { // JWT가 유효한지 확인
            System.out.println("토큰이 있고, 값도 채워줌.");
            String username = jwtUtil.getUserEmail(accessToken);
            String role = "ROLE_USER";
            User user = User.builder().email(username).build();
            // UserDetails 객체 생성 (비밀번호는 필요 없음)
            UserDetails userDetails = (UserDetails) new CustomUserDetails(user);

            // SecurityContextHolder에 인증 정보 저장
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // username, role 값을 획득
        String email = jwtUtil.getUserEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);

        User user = User.builder()
                        .email(email).build();;
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
