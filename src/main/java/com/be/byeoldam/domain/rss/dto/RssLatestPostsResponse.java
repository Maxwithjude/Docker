package com.be.byeoldam.domain.rss.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RssLatestPostsResponse {

    private Long rssId;
    private String name;
    private List<RssPostResponse> latestArticles;

    public static RssLatestPostsResponse of(Long rssId, String name, List<RssPostResponse> latestArticles) {
        return RssLatestPostsResponse.builder()
                .rssId(rssId)
                .name(name)
                .latestArticles(latestArticles)
                .build();
    }
}
