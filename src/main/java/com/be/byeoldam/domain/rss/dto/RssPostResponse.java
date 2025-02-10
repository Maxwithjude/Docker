package com.be.byeoldam.domain.rss.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RssPostResponse {

    private String title;
    private String url;
    private boolean isRead;

    public static RssPostResponse of(String title, String url, boolean isRead) {
        return RssPostResponse.builder()
                .title(title)
                .url(url)
                .isRead(isRead)
                .build();
    }
}
