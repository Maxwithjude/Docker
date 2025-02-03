package com.be.byeoldam.domain.rss.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RssPostResponse {

    private String title;
    private String url;
    private String publishedAt;
    private boolean isRead;

    public static RssPostResponse of(String title, String url, String publishedAt, boolean isRead) {
        return RssPostResponse.builder()
                .title(title)
                .url(url)
                .publishedAt(publishedAt)
                .isRead(isRead)
                .build();
    }
}
