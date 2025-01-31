package com.be.byeoldam.domain.rss.dto;

import com.be.byeoldam.domain.rss.model.Rss;
import com.be.byeoldam.domain.rss.model.UserRss;
import com.be.byeoldam.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class RssSubscribeReuest {

    private String rssUrl;

    /**
     * 유저가 RSS를 구독할 때 UserRss 엔티티를 생성하는 메서드
     */
    public UserRss toEntity(User user, Rss rss) {
        return UserRss.subscribeRss(user, rss);
    }
}
