package com.be.byeoldam.domain.user.repository;

import com.be.byeoldam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
