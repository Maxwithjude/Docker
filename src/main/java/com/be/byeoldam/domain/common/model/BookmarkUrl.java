package com.be.byeoldam.domain.common.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="bookmark_url")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "reference_count", nullable = false)
    private Long referenceCount;

    @Column(name = "reading_time")
    private int readingTime;
}
