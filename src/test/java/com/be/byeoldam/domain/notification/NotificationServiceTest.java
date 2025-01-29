package com.be.byeoldam.domain.notification;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.notification.dto.NotificationResponse;
import com.be.byeoldam.domain.notification.model.BookmarkNotification;
import com.be.byeoldam.domain.notification.model.InviteNotification;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("알림 목록 조회 메서드 테스트")
    void getNotifications() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .email("test@ssafy.com")
                .nickname("Alice")
                .password("1234")
                .build();

        SharedCollection sharedCollection = SharedCollection.createSharedCollection("Test Collection");

        InviteNotification inviteNotification = InviteNotification.builder()
                .user(user)
                .message("You are invited!")
                .collection(sharedCollection)
                .nickname("sender")
                .build();

        BookmarkUrl bookmarkUrl = BookmarkUrl.builder()
                .url("https://example.com")
                .build();

        Bookmark bookmark = Bookmark.builder()
                .bookmarkUrl(bookmarkUrl)
                .build();

        BookmarkNotification bookmarkNotification = BookmarkNotification.builder()
                .user(user)
                .message("Bookmark added!")
                .bookmark(bookmark)
                .build();

        when(notificationRepository.findByUserId(userId)).thenReturn(List.of(inviteNotification, bookmarkNotification));

        // when
        List<NotificationResponse> responses = notificationService.getNotifications(userId);

        // then
        assertThat(responses).hasSize(2);

        NotificationResponse inviteResponse = responses.get(0);
        assertThat(inviteResponse.getNotificationId()).isEqualTo(inviteNotification.getId());
        assertThat(inviteResponse.getType()).isEqualTo("INVITE");
        assertThat(inviteResponse.getTitle()).isEqualTo("Test Collection");
        assertThat(inviteResponse.getMessage()).isEqualTo("sender님이 컬렉션에 초대했습니다.");
        assertThat(inviteResponse.getUrl()).isNull();

        NotificationResponse bookmarkResponse = responses.get(1);
        assertThat(bookmarkResponse.getNotificationId()).isEqualTo(bookmarkNotification.getId());
        assertThat(bookmarkResponse.getType()).isEqualTo("BOOKMARK");
        assertThat(bookmarkResponse.getTitle()).isEqualTo("https://example.com");
        assertThat(bookmarkResponse.getMessage()).isEqualTo("Bookmark added!");
        assertThat(bookmarkResponse.getUrl()).isEqualTo("https://example.com");
    }


}