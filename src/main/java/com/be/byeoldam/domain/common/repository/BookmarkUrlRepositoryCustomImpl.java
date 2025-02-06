package com.be.byeoldam.domain.common.repository;

import com.be.byeoldam.domain.common.model.QBookmarkUrl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookmarkUrlRepositoryCustomImpl implements BookmarkUrlRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findUrlsByReference(Long cursorId, int size) {
        QBookmarkUrl bookmarkUrl = QBookmarkUrl.bookmarkUrl;

        return queryFactory
                .select(bookmarkUrl.url)
                .from(bookmarkUrl)
                .orderBy(bookmarkUrl.referenceCount.desc(), bookmarkUrl.id.desc()) // 인기순 정렬
                .offset(cursorId * size) // cursorId 기반 offset 설정
                .limit(size) // size만큼 가져오기
                .fetch();
    }

}
