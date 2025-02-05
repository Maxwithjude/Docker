package com.be.byeoldam.domain.common.repository;

import com.be.byeoldam.domain.common.model.QBookmarkUrl;
import com.be.byeoldam.domain.common.model.QTagBookmarkUrl;
import com.be.byeoldam.domain.tag.model.QTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;



@RequiredArgsConstructor
public class TagBookmarkUrlRepositoryCustomImpl implements TagBookmarkUrlRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findBookmarkUrlIdsByTagName(String tagName, Long cursorId, int size) {
        QTagBookmarkUrl tagBookmarkUrl = QTagBookmarkUrl.tagBookmarkUrl;
        QTag tag = QTag.tag;
        QBookmarkUrl bookmarkUrl = QBookmarkUrl.bookmarkUrl;

        return queryFactory
                .select(bookmarkUrl.url)
                .from(tagBookmarkUrl)
                .join(tagBookmarkUrl.tag, tag)
                .join(tagBookmarkUrl.bookmarkUrl, bookmarkUrl)
                .where(tag.name.eq(tagName))
                .orderBy(tagBookmarkUrl.bookmarkUrl.id.asc()) // ASC 정렬
                .offset(cursorId * size) // OFFSET 추가
                .limit(size) // size만큼 가져오기
                .fetch();
    }

}
