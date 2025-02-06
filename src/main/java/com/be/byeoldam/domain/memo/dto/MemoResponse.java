package com.be.byeoldam.domain.memo.dto;

import com.be.byeoldam.domain.memo.model.Memo;
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

    public static MemoResponse from(Memo memo) {
        return MemoResponse.builder()
                .memoId(memo.getId())
                .nickname(memo.getUser().getNickname())
                .content(memo.getContent())
                .imageUrl(memo.getUser().getProfileUrl())
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }
}
