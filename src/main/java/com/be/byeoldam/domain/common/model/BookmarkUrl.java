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

    @Builder
    private BookmarkUrl(String url, Long referenceCount, int readingTime) {
        this.url = url;
        this.referenceCount = referenceCount;
        this.readingTime = readingTime;
    }

    public static BookmarkUrl create(String url, Long referenceCount, int readingTime) {
        return BookmarkUrl.builder()
                        .url(url)
                        .referenceCount(referenceCount)
                        .readingTime(readingTime)
                        .build();
    }

    public void decrement() {
        this.referenceCount -= 1;
    }

    public void increment() {
        this.referenceCount += 1;
    }
}
