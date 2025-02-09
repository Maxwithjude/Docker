package com.be.byeoldam.domain.user.dto;

import com.be.byeoldam.domain.user.MypageService;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MypageImageResponse {
    private String s3Url;

    private MypageImageResponse(String s3Url){
        this.s3Url = s3Url;
    }

    public static MypageImageResponse of(String s3Url){
        return new MypageImageResponse(s3Url);
    }
}
