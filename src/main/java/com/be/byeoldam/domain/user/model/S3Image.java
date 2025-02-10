package com.be.byeoldam.domain.user.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "s3_image")
public class S3Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300, nullable = false, unique = true)
    private String fileUrl;

    @Column(length = 300, nullable = false, unique = true)
    private String fileKey;

    private S3Image(String fileUrl, String fileKey) {
        this.fileUrl = fileUrl;
        this.fileKey = fileKey;
    }

    public static S3Image of(String fileUrl, String fileKey) {
        return new S3Image(fileUrl, fileKey);
    }
}
