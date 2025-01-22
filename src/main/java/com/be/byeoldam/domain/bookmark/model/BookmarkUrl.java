package com.be.byeoldam.domain.bookmark.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="bookmark_url")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "reference_count", nullable = false)
    private Long referenceCount;

    // 타입 고민 필요
    @Column(name = "reading_time")
    private int readingTime;
}
