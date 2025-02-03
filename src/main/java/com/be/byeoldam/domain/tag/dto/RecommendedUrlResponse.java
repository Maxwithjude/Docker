package com.be.byeoldam.domain.tag.dto;

import lombok.Getter;

@Getter
public class RecommendedUrlResponse {
    private String url;
    private String title;
    private String description;
    private String image;

    public static RecommendedUrlResponse of(String url, String title, String description, String image) {
        RecommendedUrlResponse response = new RecommendedUrlResponse();
        response.url = url;
        response.title = title;
        response.description = description;
        response.image = image;
        return response;
    }
}
