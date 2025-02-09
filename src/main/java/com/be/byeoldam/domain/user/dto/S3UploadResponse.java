package com.be.byeoldam.domain.user.dto;

import com.be.byeoldam.domain.user.model.S3Image;
import lombok.Getter;

@Getter
public class S3UploadResponse {
    private final String s3Url;
    private final String s3Key;

    private S3UploadResponse(String s3Url, String s3Key){
        this.s3Url = s3Url;
        this.s3Key = s3Key;
    }

    public static S3UploadResponse of(String s3Url, String s3Key){
        return new S3UploadResponse(s3Url, s3Key);
    }

    public S3Image toEntity(){
        return S3Image.of(this.s3Url, this.s3Key);
    }
}

