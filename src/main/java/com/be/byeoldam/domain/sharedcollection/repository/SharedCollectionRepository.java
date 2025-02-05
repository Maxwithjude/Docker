package com.be.byeoldam.domain.sharedcollection.repository;

import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharedCollectionRepository extends JpaRepository<SharedCollection, Long> {

}
