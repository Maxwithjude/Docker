package com.be.byeoldam.domain.sharedcollection.repository;

import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.sharedcollection.model.SharedUser;
import com.be.byeoldam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SharedUserRepository extends JpaRepository<SharedUser, Long> {
    Optional<SharedUser> findByUserAndSharedCollection(User user, SharedCollection collection);
}
