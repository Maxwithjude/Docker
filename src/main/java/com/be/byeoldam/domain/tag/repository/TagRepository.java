package com.be.byeoldam.domain.tag.repository;

import com.be.byeoldam.domain.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {
    List<Tag> findTop10ByOrderByReferenceCountDesc();

    List<Tag> findByUserId(Long userId);

    Optional<Tag> findByName(String name);

    List<Tag> findByNameIn(List<String> names);
}
