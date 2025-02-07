package com.be.byeoldam.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserTokenRequest {
    private String refreshToken;
}
