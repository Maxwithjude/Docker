package com.be.byeoldam.domain.common.repository;

import com.be.byeoldam.domain.tag.dto.RecommendedUrlResponse;

import java.util.List;

public interface TagBookmarkUrlRepositoryCustom {
    List<RecommendedUrlResponse> findBookmarkUrlsByTagName(String tagName, Long cursorId, int size);
}
