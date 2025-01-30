package com.be.byeoldam.domain.rss.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Rss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String rssUrl;

    @Column(nullable=false)
    private String rssTitle;

    private Rss(String rssUrl, String rssTitle) {
        this.rssUrl = rssUrl;
        this.rssTitle = rssTitle;
    }

    public static Rss createRss(String rssUrl, String rssTitle) {
        return new Rss(rssUrl, rssTitle);
    }
}
