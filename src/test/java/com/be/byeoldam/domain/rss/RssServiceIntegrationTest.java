package com.be.byeoldam.domain.rss;

import com.be.byeoldam.domain.rss.dto.RssPostResponse;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RssServiceIntegrationTest {
    private final RssService rssService = new RssService(null, null, null);

    @Test
    void fetchRssPosts_실제_RSS_가져오기() {
        // given
        String rssUrl = "https://rss.blog.naver.com/eunrosophy.xml"; // 네이버 블로그 RSS
        Pageable pageable = PageRequest.of(0, 5); // 첫 페이지에서 5개만 가져오기

        // when
        Page<RssPostResponse> result = rssService.fetchRssPosts(rssUrl, null, pageable);

        // then
        assertThat(result).isNotEmpty(); // 적어도 한 개 이상의 RSS 글이 있어야 함
        assertThat(result.getContent().size()).isLessThanOrEqualTo(5); // 5개 이하일 것
        assertThat(result.getContent().get(0).getTitle()).isNotBlank(); // 제목이 비어있으면 안 됨
        List<RssPostResponse> posts = result.getContent();

        for (RssPostResponse post : posts) {
            System.out.println(post.getTitle());
        }
    }

    @Test
    void extractRssName_실제_RSS_테스트() {
        // given
        String rssUrl = "https://rss.blog.naver.com/eunrosophy.xml"; // 네이버 블로그 RSS

        // when
        String rssName = rssService.extractRssName(rssUrl);

        // then
        assertThat(rssName).isNotBlank(); // 제목이 비어있으면 안 됨
        System.out.println("RSS 제목: " + rssName);
    }


    @Test
    void isSubscribed_네이버_블로그_테스트() {
        // given
        String url = "https://blog.naver.com/eunrosophy/223324773246"; // 네이버 블로그 주소

        // when
        boolean result = rssService.isSubscribed(url);

        // then
        assertThat(result).isTrue(); // RSS 지원 여부 확인
    }

    @Test
    void findRssUrl_네이버_블로그_테스트() {
        // given
        String siteUrl = "https://blog.naver.com/eunrosophy/223324773246"; // 블로그 주소

        // when
        String rssUrl = rssService.findRssUrl(siteUrl);

        // then
        assertThat(rssUrl).isNotBlank(); // RSS URL이 비어있으면 안 됨
        assertThat(rssUrl).contains("rss.blog.naver.com"); // 네이버 블로그 RSS 패턴 확인
        System.out.println("찾은 RSS URL: " + rssUrl);
    }
}
