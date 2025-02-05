package com.be.byeoldam.domain.common.repository;

import java.util.List;

public interface TagBookmarkUrlRepositoryCustom {
    List<String> findBookmarkUrlIdsByTagName(String tagName, Long cursorId, int size);
}
