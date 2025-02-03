package com.be.byeoldam.domain.rss;

import com.be.byeoldam.domain.rss.dto.RssLatestPostsResponse;
import com.be.byeoldam.domain.rss.dto.RssPostResponse;
import com.be.byeoldam.domain.rss.dto.RssSubscribeRequest;
import com.be.byeoldam.domain.rss.dto.UserRssResponse;
import com.be.byeoldam.domain.rss.model.Rss;
import com.be.byeoldam.domain.rss.model.UserRss;
import com.be.byeoldam.domain.rss.repository.RssRepository;
import com.be.byeoldam.domain.rss.repository.UserRssRepository;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RssService {

    private final RssRepository rssRepository;
    private final UserRssRepository userRssRepository;
    private final UserRepository userRepository;

    @Transactional
    public void subscribeRss(Long userId, RssSubscribeRequest rssSubscribeRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        String rssUrl = findRssUrl(rssSubscribeRequest.getSiteUrl()); // RSS URL 추출

        Rss rss = rssRepository.findByRssUrl(rssUrl)
                .orElseGet(() -> {
                    String rssName = extractRssName(rssUrl);
                    return rssRepository.save(Rss.createRss(rssUrl, rssName));
                });

        // 이미 구독한 RSS인지 확인
        if (userRssRepository.existsByUserIdAndRssId(userId, rss.getId())) {
            throw new CustomException("이미 구독한 RSS입니다.");
        }

        userRssRepository.save(rssSubscribeRequest.toEntity(user, rss));
    }



    @Transactional
    public void unsubscribeRss(Long userId, Long rssId) {

        if (!userRepository.existsById(userId)) {
            throw new CustomException("사용자를 찾을 수 없습니다.");
        }

        if (!rssRepository.existsById(rssId)) {
            throw new CustomException("RSS를 찾을 수 없습니다.");
        }

        if (!userRssRepository.existsByUserIdAndRssId(userId, rssId)) {
            throw new CustomException("구독하지 않은 RSS입니다.");
        }

        userRssRepository.deleteByUserIdAndRssId(userId, rssId);
    }

    /**
     * 사용자의 RSS 구독 목록을 조회하는 메서드
     * @param userId
     * @return
     */
    @Transactional
    public List<UserRssResponse> getUserRssList(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new CustomException("사용자를 찾을 수 없습니다.");
        }

        List<UserRss> userRssList = userRssRepository.findByUserId(userId);

        return userRssList.stream()
                .map(userRss -> UserRssResponse.of(
                        userRss.getRss().getId(),
                        userRss.getRss().getName(),
                        userRss.isRead()
                ))
                .toList();
    }

    /**
     * 특정 RSS 최신 글 목록을 조회하는 메서드
     * @param userId
     * @param rssId
     * @return
     */
    @Transactional
    public RssLatestPostsResponse getRssLatestArticles(Long userId, Long rssId, Pageable pageable) {

        if (!userRepository.existsById(userId)) {
            throw new CustomException("사용자를 찾을 수 없습니다.");
        }

        Rss rss = rssRepository.findById(rssId)
                .orElseThrow(() -> new CustomException("RSS를 찾을 수 없습니다."));

        UserRss userRss = userRssRepository.findByUserIdAndRssId(userId, rssId)
                .orElseThrow(() -> new CustomException("구독하지 않은 RSS입니다."));

        String latestTitle = userRss.getLatestTitle();
        String previousTitle = userRss.getPreviousTitle();


        Page<RssPostResponse> latestArticles = fetchRssPosts(rss.getRssUrl(), previousTitle, pageable);

        // 첫 페이지 요청 시에만 latestTitle 업데이트
        if (pageable.getPageNumber() == 0 && !latestArticles.isEmpty()) {
            String newestTitle = latestArticles.getContent().get(0).getTitle();
            userRss.updateTitles(newestTitle);
        }

        return RssLatestPostsResponse.of(
                rss.getId(),
                rss.getName(),
                latestArticles
        );
    }

    // RSS의 최신 글 목록을 가져오는 메서드
    private Page<RssPostResponse> fetchRssPosts(String rssUrl, String previousTitle, Pageable pageable) {
        try {
            // RSS 피드 가져오기
            URL feedSource = new URL(rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));

            int totalSize = feed.getEntries().size(); // 전체 글 개수

            // 글이 없는 경우
            if (feed.getEntries().isEmpty()) {
                return Page.empty(pageable);
            }

            // 페이징 처리
            List<RssPostResponse> pagedArticles = feed.getEntries().stream()
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .map(entry -> {
                        String title = entry.getTitle();
                        String link = entry.getLink();

                        // previousTitle과 비교하여 is_read 판별
                        boolean isRead = previousTitle != null && title.compareTo(previousTitle) <= 0;

                        return RssPostResponse.builder()
                                .title(title)
                                .url(link)
                                .isRead(isRead)
                                .build();
                    }).toList();

            return new PageImpl<>(pagedArticles, pageable, totalSize);

        } catch (Exception e) {
            throw new CustomException("RSS 피드를 가져오는 중 오류가 발생했습니다.");
        }
    }

    /**
     * RSS 이름 추출 메서드
     */
    private String extractRssName(String rssUrl) {
        try {
            URL feedSource = new URL(rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));

            return feed.getTitle(); // RSS 피드 제목 반환
        } catch (Exception e) {
            throw new CustomException("RSS 제목을 가져오는 중 오류가 발생했습니다.");
        }
    }

    /**
     * RSS 구독 가능한 URL인지 확인하는 메서드
     */
    public boolean isSubscribed(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("link[type=application/rss+xml]");
            return !links.isEmpty();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * RSS URL 추출 메서드
     * @param siteUrl
     * @return
     */
    private String findRssUrl(String siteUrl) {
        try {
            Document doc = Jsoup.connect(siteUrl).get();
            Elements links = doc.select("link[type=application/rss+xml], link[type=application/atom+xml]");
            if (links.isEmpty()) {
                throw new IOException("No RSS URL found");
            }
            for (Element link : links) {
                String rssUrl = link.attr("href");

                // 상대경로 처리 (예: "/rss.xml" → "https://example.com/rss.xml")
                if (!rssUrl.startsWith("http")) {
                    rssUrl = siteUrl + (rssUrl.startsWith("/") ? rssUrl : "/" + rssUrl);
                }

                return rssUrl;
            }

        } catch (IOException e) {
            throw new CustomException("RSS URL을 찾을 수 없습니다.");
        }

        return null;
    }
}
