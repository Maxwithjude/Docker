package com.be.byeoldam.domain.user.dto;

import lombok.Getter;

@Getter
public class UserTokenResponse {
    private String accessToken;
    private String refreshToken;

    private UserTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static UserTokenResponse of(String accessToken, String refreshToken) {
        return new UserTokenResponse(accessToken, refreshToken);
    }
}
