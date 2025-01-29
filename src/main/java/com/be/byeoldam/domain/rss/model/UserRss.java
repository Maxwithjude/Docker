package com.be.byeoldam.domain.rss.model;

import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserRss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rss_id", nullable=false)
    private Rss rss;

    private String latestTitle;
    private boolean isRead;

    private UserRss(User user, Rss rss, String latestTitle, boolean isRead) {
        this.user = user;
        this.rss = rss;
        this.latestTitle = latestTitle;
        this.isRead = isRead;
    }

    public static UserRss subscribeRss(User user, Rss rss, String latestTitle) {
        return new UserRss(user, rss, latestTitle, false);
    }

    public void updateRss(String latestTitle) {
        this.latestTitle = latestTitle;
        this.isRead = true;
    }
}
