package com.be.byeoldam.common.filter;

import com.be.byeoldam.common.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("-----LoginFilter.attemptAuthentication-----");

        //클라이언트 요청에서 email, password 추출(form-data로 던져줘야 함..)
        String email = obtainEmail(request);
        String password = obtainPassword(request);
        System.out.println(email+"," + password);
        //스프링 시큐리티에서 email과 password를 검증하기 위해서는 token에 담아야 함, 세번째 인자는 ROLE.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        //token에 담은 후 검증을 위해 AuthenticationManager에 전달
        return authenticationManager.authenticate(authToken);
    }

    protected String obtainEmail(HttpServletRequest request) {
        return request.getParameter("email");
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        System.out.println("-----LoginFilter.successfulAuthentication-----");
        String token = jwtUtil.createJwt("access",1,"usad63","ROLE_USER",1000000000000000l);
        // 필터 체인 계속 진행 → 컨트롤러로 요청이 넘어감
        response.setHeader("access", token); //access token은 Header
        response.setStatus(HttpStatus.OK.value());

    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException failed) throws IOException, ServletException {
        System.out.println("-----LoginFilter.unsuccessfulAuthentication-----");
    }
}
