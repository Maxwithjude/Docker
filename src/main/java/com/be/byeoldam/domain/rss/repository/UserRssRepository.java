package com.be.byeoldam.domain.rss.repository;

import com.be.byeoldam.domain.rss.model.UserRss;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRssRepository extends JpaRepository<UserRss, Long> {
    boolean existsByUserIdAndRssId(Long userId, Long id);

    void deleteByUserIdAndRssId(Long userId, Long rssId);

    List<UserRss> findByUserId(Long userId);

    Optional<UserRss> findByUserIdAndRssId(Long userId, Long rssId);
}
