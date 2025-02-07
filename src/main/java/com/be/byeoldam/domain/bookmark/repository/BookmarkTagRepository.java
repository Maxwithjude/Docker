package com.be.byeoldam.domain.bookmark.repository;

import com.be.byeoldam.domain.bookmark.model.BookmarkTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkTagRepository extends JpaRepository<BookmarkTag, Long> {
}
