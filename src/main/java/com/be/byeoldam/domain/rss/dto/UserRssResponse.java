package com.be.byeoldam.domain.rss.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRssResponse {

    private Long rssId;
    private String name; // 구독 채널 이름
    private boolean isRead;

    public static UserRssResponse of(Long rssId, String name, boolean isRead) {
        return UserRssResponse.builder()
                .rssId(rssId)
                .name(name)
                .isRead(isRead)
                .build();
    }

}
