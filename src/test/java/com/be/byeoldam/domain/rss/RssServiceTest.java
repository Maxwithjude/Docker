package com.be.byeoldam.domain.rss;

import com.be.byeoldam.domain.rss.dto.RssLatestPostsResponse;
import com.be.byeoldam.domain.rss.dto.RssPostResponse;
import com.be.byeoldam.domain.rss.dto.RssSubscribeRequest;
import com.be.byeoldam.domain.rss.dto.UserRssResponse;
import com.be.byeoldam.domain.rss.model.Rss;
import com.be.byeoldam.domain.rss.model.UserRss;
import com.be.byeoldam.domain.rss.repository.RssRepository;
import com.be.byeoldam.domain.rss.repository.UserRssRepository;
import com.be.byeoldam.domain.rss.service.RssService;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RssServiceTest {

    @Spy
    @InjectMocks
    private RssService rssService;

    @Mock
    private RssRepository rssRepository;

    @Mock
    private UserRssRepository userRssRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Rss rss;
    private UserRss userRss;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        ReflectionTestUtils.setField(user, "id", 1L);

        rss = Rss.createRss("https://blog.naver.com/eunrosophy/223324773246", "Example RSS");
        ReflectionTestUtils.setField(rss, "id", 10L); // ID 설정

        userRss = UserRss.subscribeRss(user, rss);
    }

    @Test
    @DisplayName("정상적인 RSS 구독 테스트")
    void subscibe_success() {
        // Given
        RssSubscribeRequest request = new RssSubscribeRequest("https://blog.naver.com/eunrosophy/223324773246");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(rssRepository.findByRssUrl(anyString())).thenReturn(Optional.empty());
        when(rssRepository.save(any())).thenReturn(rss);
        when(userRssRepository.existsByUserIdAndRssId(1L, 10L)).thenReturn(false);

        // When
        rssService.subscribeRss(1L, request);

        // Then
        verify(userRssRepository).save(any());
    }

    @Test
    @DisplayName("이미 구독한 RSS를 구독하려고 할 때 예외 발생")
    void subscibe_already_subscribed() {
        // Given
        RssSubscribeRequest request = new RssSubscribeRequest("https://blog.naver.com/eunrosophy/223324773246");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(rssRepository.findByRssUrl(anyString())).thenReturn(Optional.of(rss));
        when(userRssRepository.existsByUserIdAndRssId(1L, 10L)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> rssService.subscribeRss(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 구독한 RSS입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 사용자가 구독하려고 할 때 예외 발생")
    void subscibe_not_exist_user() {
        // Given
        RssSubscribeRequest request = new RssSubscribeRequest("https://blog.naver.com/eunrosophy/223324773246");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> rssService.subscribeRss(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("구독 취소 성공")
    void unsubscribe_success() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(rssRepository.existsById(10L)).thenReturn(true);
        when(userRssRepository.existsByUserIdAndRssId(1L, 10L)).thenReturn(true);

        // When
        rssService.unsubscribeRss(1L, 10L);

        // Then
        verify(userRssRepository).deleteByUserIdAndRssId(1L, 10L);
    }

    @Test
    @DisplayName("구독하지_않은_RSS_구독취소시_예외발생")
    void unsubscribe_not_subscribed() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(rssRepository.existsById(10L)).thenReturn(true);
        when(userRssRepository.existsByUserIdAndRssId(1L, 10L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> rssService.unsubscribeRss(1L, 10L))
                .isInstanceOf(CustomException.class)
                .hasMessage("구독하지 않은 RSS입니다.");
    }

    @Test
    @DisplayName("사용자의_RSS_구독목록_정상조회")
    void getUserRssList_success() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRssRepository.findByUserId(1L)).thenReturn(List.of(userRss));

        // 기존 latestTitle 설정
        userRss.updateTitles("Old Title");

        // 최신 RSS 제목이 다름 (새로운 글이 있음)
        doReturn("New Title").when(rssService).extractLatestTitle(userRss.getRss().getRssUrl());

        // When
        List<UserRssResponse> response = rssService.getUserRssList(1L);

        // Then
        assertThat(response).isNotEmpty();
        assertThat(response.get(0).getRssId()).isEqualTo(10L);
        assertThat(response.get(0).getName()).isEqualTo("Example RSS");
        assertThat(response.get(0).isRead()).isFalse(); // 새로운 글이 있어서 false로 변경되었어야 함

        verify(userRssRepository).findByUserId(1L);
        verify(rssService).extractLatestTitle(userRss.getRss().getRssUrl());
    }

    @Test
    @DisplayName("존재하지_않는_사용자의_RSS_조회시_예외발생")
    void getUserRssList_not_exist_user() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> rssService.getUserRssList(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("RSS_피드에서_제목_가져오는_중_오류발생시_예외처리")
    void getUserRssList_error_extracting_title() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRssRepository.findByUserId(1L)).thenReturn(List.of(userRss));

        doThrow(new CustomException("RSS 피드를 가져오는 중 오류가 발생했습니다."))
                .when(rssService).extractLatestTitle(userRss.getRss().getRssUrl());

        // When & Then
        assertThatThrownBy(() -> rssService.getUserRssList(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage("RSS 피드를 가져오는 중 오류가 발생했습니다.");
    }

    @Test
    @DisplayName("특정_RSS_최신_글_조회_정상작동")
    void 특정_RSS_최신_글_조회_정상작동() {
        // Given
        Long userId = 1L;
        Long rssId = 10L;
        Pageable pageable = PageRequest.of(0, 5);

        List<RssPostResponse> posts = List.of(
                RssPostResponse.of("New Article 1", "https://example.com/1", false),
                RssPostResponse.of("New Article 2", "https://example.com/2", false)
        );
        Page<RssPostResponse> postPage = new PageImpl<>(posts, pageable, posts.size());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(rssRepository.findById(rssId)).thenReturn(Optional.of(rss));
        when(userRssRepository.findByUserIdAndRssId(userId, rssId)).thenReturn(Optional.of(userRss));
        doReturn(postPage)
                .when(rssService)
                .fetchRssPosts(rss.getRssUrl(), userRss.getPreviousTitle(), pageable);


        // When
        RssLatestPostsResponse response = rssService.getRssLatestArticles(userId, rssId, pageable);

        // Then
        assertThat(response)
                .isNotNull()
                .extracting(RssLatestPostsResponse::getRssId, RssLatestPostsResponse::getName)
                .containsExactly(rssId, "Example RSS");

        assertThat(response.getLatestPosts())
                .isNotEmpty()
                .hasSize(2)
                .allSatisfy(post -> {
                    assertThat(post.getTitle()).isNotBlank();
                    assertThat(post.getUrl()).isNotBlank();
                });

        // 첫 페이지 요청일 때만 latestTitle 업데이트 확인
        assertThat(userRss.getLatestTitle()).isEqualTo("New Article 1");
    }

    @Test
    void 최신글이_없으면_빈_리스트_반환() {
        // Given
        Long userId = 1L;
        Long rssId = 10L;
        Pageable pageable = PageRequest.of(0, 5);
        Page<RssPostResponse> emptyPage = Page.empty(pageable);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(rssRepository.findById(rssId)).thenReturn(Optional.of(rss));
        when(userRssRepository.findByUserIdAndRssId(userId, rssId)).thenReturn(Optional.of(userRss));
        doReturn(emptyPage)
                .when(rssService)
                .fetchRssPosts(rss.getRssUrl(), userRss.getPreviousTitle(), pageable);

        // When
        RssLatestPostsResponse response = rssService.getRssLatestArticles(userId, rssId, pageable);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getLatestPosts()).isEmpty();
    }

}