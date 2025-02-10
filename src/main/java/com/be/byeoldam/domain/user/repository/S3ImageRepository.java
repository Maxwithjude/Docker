package com.be.byeoldam.domain.user.repository;

import com.be.byeoldam.domain.user.model.S3Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface S3ImageRepository extends JpaRepository<S3Image, Long> {
    Optional<S3Image> findByFileUrl(String fileUrl);
}
