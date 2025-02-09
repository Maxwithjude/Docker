package com.be.byeoldam.domain.common.repository;

import com.be.byeoldam.domain.common.model.BookmarkUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkUrlRepository extends JpaRepository<BookmarkUrl, Long>, BookmarkUrlRepositoryCustom {
    Optional<BookmarkUrl> findByUrl(String url);
}
