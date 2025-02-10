package com.be.byeoldam.domain.user.dto;

import lombok.Getter;

@Getter
public class ChangePasswordResponse {
    private final boolean isChanged;

    private ChangePasswordResponse(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public static ChangePasswordResponse of(boolean isChanged) {
        return new ChangePasswordResponse(isChanged);
    }
}
