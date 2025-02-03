package com.be.byeoldam.domain.personalcollection.repository;

import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PersonalCollectionRepository extends JpaRepository<PersonalCollection, Long> {
    // 개인컬렉션 목록 조회
    List<PersonalCollection> findByUser(User user);

    // 유저가 같은 이름의 개인컬렉션을 가지고 있는지 확인
    Optional<PersonalCollection> findByUserAndName(User user, String name);
}

