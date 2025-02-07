package com.be.byeoldam.domain.rss.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class RssLatestPostsResponse {

    private Long rssId;
    private String name;
    private Page<RssPostResponse> latestPosts;

    public static RssLatestPostsResponse of(Long rssId, String name, Page<RssPostResponse> latestPosts) {
        return RssLatestPostsResponse.builder()
                .rssId(rssId)
                .name(name)
                .latestPosts(latestPosts)
                .build();
    }
}
