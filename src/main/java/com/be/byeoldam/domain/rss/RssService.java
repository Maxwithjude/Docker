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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        String rssUrl = rssSubscribeRequest.getRssUrl();

        Rss rss = rssRepository.findByRssUrl(rssUrl)
                .orElseGet(() -> rssRepository.save(Rss.createRss(rssUrl, extractRssName(rssUrl))));

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

    @Transactional
    public RssLatestPostsResponse getRssLatestArticles(Long userId, Long rssId) {

        if (!userRepository.existsById(userId)) {
            throw new CustomException("사용자를 찾을 수 없습니다.");
        }

        Rss rss = rssRepository.findById(rssId)
                .orElseThrow(() -> new CustomException("RSS를 찾을 수 없습니다."));

        UserRss userRss = userRssRepository.findByUserIdAndRssId(userId, rssId)
                .orElseThrow(() -> new CustomException("구독하지 않은 RSS입니다."));

        String latestTitle = userRss.getLatestTitle();

        List<RssPostResponse> latestArticles = fetchRssArticles(rss.getRssUrl(), latestTitle);

        // todo 최신 글 제목 업데이트 로직

        return RssLatestPostsResponse.of(
                rss.getId(),
                rss.getName(),
                latestArticles
        );
    }

    // todo : RSS의 최신 글 목록을 가져오는 메서드
    private List<RssPostResponse> fetchRssArticles(String rssUrl, String latestTitle) {
        return null;
    }

    //todo : RSS 이름 추출
    private String extractRssName(String rssUrl) {
        return null;
    }

}
