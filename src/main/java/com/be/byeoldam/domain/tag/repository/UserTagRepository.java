package com.be.byeoldam.domain.tag.repository;

import com.be.byeoldam.domain.tag.model.Tag;
import com.be.byeoldam.domain.tag.model.UserTag;
import com.be.byeoldam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    boolean existsByUserAndTag(User user, Tag tag);
    void deleteByUser(User user);
}
