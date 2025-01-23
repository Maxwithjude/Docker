package com.be.byeoldam.domain.common.repository.tag;

import com.be.byeoldam.domain.common.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {

}
