package com.be.byeoldam.domain.memo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemoResponse {

    private Long memoId;
    private String nickname;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemoResponse of(Long memoId, String nickname, String content, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return MemoResponse.builder()
                .memoId(memoId)
                .nickname(nickname)
                .content(content)
                .imageUrl(imageUrl)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
