package com.be.byeoldam.domain.user.dto;

import com.be.byeoldam.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserLoginResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;

    private UserLoginResponse(Long userId, String email, String nickname){
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
    }

    public static UserLoginResponse fromEntity(User user){
        return new UserLoginResponse(user.getId(), user.getEmail(), user.getEmail());
    }

    public void addTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
