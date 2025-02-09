package com.be.byeoldam.domain.common.repository;

import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.common.model.TagBookmarkUrl;
import com.be.byeoldam.domain.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagBookmarkUrlRepository extends JpaRepository<TagBookmarkUrl, Long>, TagBookmarkUrlRepositoryCustom {

    void deleteByUrl(BookmarkUrl url);

    void deleteByTag(Tag tag);
}
