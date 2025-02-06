package com.be.byeoldam.config;

import com.be.byeoldam.common.filter.LoginFilter;
import com.be.byeoldam.common.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;


    // 비밀번호 암호화를 위해 사용(Spring Security에서 제공)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception{
        //csrf보호를 비활성화.
        http.csrf((auth) -> auth.disable())
        .cors(cors -> cors.configure(http)); // ✅ CORS 설정 추가(swagger 사용 시 필요)

        //From 로그인 방식 비활성화  jwt 방식을 사용
        http.formLogin((auth) -> auth.disable());
        //http basic 인증 비활성화
        http.httpBasic((auth) -> auth.disable());
        // Spring Security Level 에서는 Session 사용 비활성화, Spring MVC Level에서 직접 사용하는 것은 가능.
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /*
         * 경로별 인가 작업. 모든 요청은 인증된 사용자만 접근 가능하며, 인증되지 않은 사용자가 접근하면 401에러를 발생.
         * Spring Security는 역할 이름 앞에 "ROLE_"라는 접두사를 자동으로 붙임 -> hasRole("ADMIN")은 ROLE_ADMIN을 검사
         * */
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml", "/v3/api-docs/swagger-config").permitAll()
                .requestMatchers("/login", "/", "/join","/reissue").permitAll() //모든 사용자
                .requestMatchers("/admin").hasRole("ADMIN") //role이 ADMIN인 유저만(추후에 수정하기)
                .anyRequest().authenticated()); //나머지는 로그인한 유저만

        //필터 추가, 첫번재 인자의 필터를 두번째 인자로 온 필터자리에 대체함
        http.addFilterAt(new LoginFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //        // 해당 필터 사용하니까 /admin에 접속 가능
//        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
//        // 커스텀한 로그아웃 필터 사용하기
//        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        return http.build();
    }

}
