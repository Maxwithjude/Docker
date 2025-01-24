package com.be.byeoldam.domain.personalcollection.repository;

import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalCollectionRepository extends JpaRepository<PersonalCollection, Long> {
    List<PersonalCollection> findByUserId(Long userId);
}
