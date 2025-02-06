package com.be.byeoldam.domain.bookmark.dto;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookmarkRequest {
    private String url;
    private String collection;
    private String collectionType;
    private int readingTime;
//
//    public Bookmark toEntity() {
//        return Bookmark.create();
//    }

}
