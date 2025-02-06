package com.be.byeoldam.domain.common.repository;

import com.be.byeoldam.domain.common.model.TagBookmarkUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagBookmarkUrlRepository extends JpaRepository<TagBookmarkUrl, Long>, TagBookmarkUrlRepositoryCustom {

}
