package com.be.byeoldam.domain.rss;

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
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
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
import static org.springframework.data.domain.PageRequest.*;

@ExtendWith(MockitoExtension.class)
class RssServiceTest {

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

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        ReflectionTestUtils.setField(user, "id", 1L);

        rss = Rss.createRss("https://blog.naver.com/eunrosophy/223324773246", "Example RSS");
        ReflectionTestUtils.setField(rss, "id", 10L); // ID 설정
    }

    @Test
    void 구독_정상동작() {
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
    void 이미_구독한_RSS_구독시_예외발생() {
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
    void 존재하지_않는_사용자가_구독시_예외발생() {
        // Given
        RssSubscribeRequest request = new RssSubscribeRequest("https://blog.naver.com/eunrosophy/223324773246");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> rssService.subscribeRss(1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    void 구독_취소_정상동작() {
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
    void 구독하지_않은_RSS_구독취소시_예외발생() {
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
    void 사용자의_RSS_구독목록_정상조회() {
        // Given
        UserRss userRss = UserRss.subscribeRss(user, rss);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRssRepository.findByUserId(1L)).thenReturn(List.of(userRss));

        // When
        List<UserRssResponse> result = rssService.getUserRssList(1L);

        // Then
        assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .allSatisfy(response -> {
                    assertThat(response.getRssId()).isEqualTo(10L);
                    assertThat(response.getName()).isEqualTo("Example RSS");
                    assertThat(response.isRead()).isFalse();
                });
    }

    @Test
    void 존재하지_않는_사용자의_RSS_조회시_예외발생() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> rssService.getUserRssList(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

}