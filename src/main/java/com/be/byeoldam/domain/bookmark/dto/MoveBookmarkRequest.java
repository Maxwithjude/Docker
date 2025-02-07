package com.be.byeoldam.domain.bookmark.dto;

import lombok.Getter;

@Getter
public class MoveBookmarkRequest {
    private Long collectionId;
    private boolean isPersonal;
}
