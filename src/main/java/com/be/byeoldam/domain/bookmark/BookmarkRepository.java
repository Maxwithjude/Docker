package com.be.byeoldam.domain.bookmark;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
