package com.be.byeoldam.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MypageRequest {
    String nickname;
    int alertDay;
    List<String> tagList;
}
