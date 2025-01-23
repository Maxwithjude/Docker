package com.be.byeoldam.domain.user.dto;

import com.be.byeoldam.domain.user.model.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRegisterRequest {
    private String email;
    private String password;
    private String nickname;

    public User toEntity(){
        return User.builder().email(email).password(password).nickname(nickname).build();
    }
}
