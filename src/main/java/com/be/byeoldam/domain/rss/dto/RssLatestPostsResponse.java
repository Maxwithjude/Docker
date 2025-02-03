package com.be.byeoldam.domain.rss.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class RssLatestPostsResponse {

    private Long rssId;
    private String name;
    private Page<RssPostResponse> latestArticles;

    public static RssLatestPostsResponse of(Long rssId, String name, Page<RssPostResponse> latestArticles) {
        return RssLatestPostsResponse.builder()
                .rssId(rssId)
                .name(name)
                .latestArticles(latestArticles)
                .build();
    }
}
