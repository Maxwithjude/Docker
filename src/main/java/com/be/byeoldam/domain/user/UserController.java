package com.be.byeoldam.domain.user;

import com.be.byeoldam.common.jwt.JwtUtil;
import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterRequest request) {
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.createJwt("1",1,"222","f",10l);

        return ResponseEntity.ok().header("access", token).body("회원 가입 성공");
    }

    @PostMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/me")
    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "로그인한 유저가 아닙니다.";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return "현재 로그인한 유저: " + userDetails.getUsername();
    }

    @GetMapping("/")
    public String mainP(){
        return "/";
    }



    @GetMapping("/signUp")
    public String signUp(){
        return "signUp";
    }

    @GetMapping("/reissue")
    public String reissue(){
        return "reissue";
    }

    @GetMapping("/not")
    public String not(){
        return "not";
    }

}
