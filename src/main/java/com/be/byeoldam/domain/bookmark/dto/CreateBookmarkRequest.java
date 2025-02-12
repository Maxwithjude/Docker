package com.be.byeoldam.domain.bookmark.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBookmarkRequest {
    private String url;
    private Long collectionId;
    private List<TagDto> tags;
    private boolean personal;
}
