package com.be.byeoldam.domain.rss.repository;

import com.be.byeoldam.domain.rss.model.Rss;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RssRepository extends JpaRepository<Rss, Long> {

    Optional<Rss> findByRssUrl(String rssUrl);
}
