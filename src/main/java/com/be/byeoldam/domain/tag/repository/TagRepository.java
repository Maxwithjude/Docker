package com.be.byeoldam.domain.tag.repository;

import com.be.byeoldam.domain.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {
    List<Tag> findTop10ByOrderByReferenceCountDesc();
}
