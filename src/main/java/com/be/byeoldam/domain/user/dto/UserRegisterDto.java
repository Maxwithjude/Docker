package com.be.byeoldam.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    private String email;
    private String password;
    private String nickname;
}
