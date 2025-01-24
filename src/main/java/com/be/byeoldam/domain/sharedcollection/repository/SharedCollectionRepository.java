package com.be.byeoldam.domain.sharedcollection.repository;


import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedCollectionRepository extends JpaRepository<SharedCollection, Long> {

}
