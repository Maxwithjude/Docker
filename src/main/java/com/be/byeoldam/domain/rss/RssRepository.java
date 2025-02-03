package com.be.byeoldam.domain.rss;

import com.be.byeoldam.domain.rss.model.Rss;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RssRepository extends JpaRepository<Rss, Long> {

}
