package com.be.byeoldam.domain.common.repository;

import java.util.List;

public interface BookmarkUrlRepositoryCustom {
    List<String> findUrlsByReference(Long cursorId, int size);
}
