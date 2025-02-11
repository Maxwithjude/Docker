package com.be.byeoldam.domain.bookmark.repository;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b WHERE b.user.id = :userId AND b.isRead = false AND b.createdAt <= :targetDate")
    List<Bookmark> findUnreadBookmarksByUserIdAndDate(@Param("userId") Long userId, @Param("targetDate") LocalDateTime targetDate);

    Optional<Bookmark> findByBookmarkUrlAndUser(BookmarkUrl bookmarkUrl, User user);

    List<Bookmark> findByUrlAndUser(BookmarkUrl bookmarkUrl, User user);
}
