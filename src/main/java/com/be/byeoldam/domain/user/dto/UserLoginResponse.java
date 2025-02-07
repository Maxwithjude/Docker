package com.be.byeoldam.domain.user.dto;

import com.be.byeoldam.domain.user.model.User;
import lombok.Getter;

@Getter
public class UserLoginResponse {
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;

    private UserLoginResponse(String email, String nickname){
        this.email = email;
        this.nickname = nickname;
    }

    public static UserLoginResponse fromEntity(User user){
        return new UserLoginResponse(user.getEmail(), user.getEmail());
    }

    public void addTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
