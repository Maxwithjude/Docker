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
    private String previousTitle; // 이전 요청에서의 latestTitle
    private boolean isRead;

    private UserRss(User user, Rss rss, String latestTitle, String previousTitle, boolean isRead) {
        this.user = user;
        this.rss = rss;
        this.latestTitle = latestTitle;
        this.previousTitle = previousTitle;
        this.isRead = isRead;
    }

    public static UserRss subscribeRss(User user, Rss rss) {
        return new UserRss(user, rss, null, null, false);
    }

    public void updateTitles(String newLatestTitle) {
        this.previousTitle = this.latestTitle; // 기존 latestTitle을 previousTitle로 이동
        this.latestTitle = newLatestTitle;    // 새로운 최신글 제목 업데이트
        this.isRead = true;  // 새로운 글이 있기 때문에 읽음 상태 변경
    }
}
