package com.be.byeoldam.domain.sharedcollection.repository;

import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedCollectionRepository extends JpaRepository<SharedCollection, Long> {
}
