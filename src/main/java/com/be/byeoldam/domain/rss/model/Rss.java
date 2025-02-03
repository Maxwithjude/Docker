package com.be.byeoldam.domain.rss.model;

import jakarta.persistence.*;
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
    private String name; // 구독 채널 이름

    private Rss(String rssUrl, String name) {
        this.rssUrl = rssUrl;
        this.name = name;
    }

    public static Rss createRss(String rssUrl, String name) {
        return new Rss(rssUrl, name);
    }
}
