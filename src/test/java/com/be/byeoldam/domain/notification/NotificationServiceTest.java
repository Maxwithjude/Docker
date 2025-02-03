package com.be.byeoldam.domain.notification;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.notification.dto.NotificationResponse;
import com.be.byeoldam.domain.notification.model.BookmarkNotification;
import com.be.byeoldam.domain.notification.model.InviteNotification;
import com.be.byeoldam.domain.notification.model.Notification;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    private BookmarkNotification bookmarkNotification;
    private InviteNotification inviteNotification;
    private User user;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        ReflectionTestUtils.setField(user, "id", 100L);

        BookmarkUrl bookmarkUrl = BookmarkUrl.builder().url("https://www.example.com").build();
        Bookmark bookmark = Bookmark.builder().bookmarkUrl(bookmarkUrl).build();

        SharedCollection sharedCollection = SharedCollection.createSharedCollection("컬렉션 이름");

        bookmarkNotification = new BookmarkNotification(user, "이 포스트를 추가한 지 7일 경과되었습니다.", bookmark);
        ReflectionTestUtils.setField(bookmarkNotification, "id", 1L);
        ReflectionTestUtils.setField(bookmarkNotification, "createdAt", LocalDateTime.of(2025, 1, 17, 12, 34, 56));

        inviteNotification = new InviteNotification(user, "공유컬렉션에 사용자가 초대되었습니다.", sharedCollection, "초대자닉네임");
        ReflectionTestUtils.setField(inviteNotification, "id", 2L);
        ReflectionTestUtils.setField(inviteNotification, "createdAt", LocalDateTime.of(2025, 1, 17, 12, 34, 56));
    }

    @Test
    @DisplayName("알림 목록 조회 메서드 테스트")
    void getNotifications() {
        // given
        Long userId = 100L;
        List<Notification> notifications = List.of(bookmarkNotification, inviteNotification);
        when(notificationRepository.findByUserId(userId)).thenReturn(notifications);

        // when
        List<NotificationResponse> responses = notificationService.getNotifications(userId);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getNotificationId()).isEqualTo(1L);
        assertThat(responses.get(0).getType()).isEqualTo("BOOKMARK");
        assertThat(responses.get(0).getMessage()).isEqualTo("이 포스트를 추가한 지 7일 경과되었습니다.");
        assertThat(responses.get(0).getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 1, 17, 12, 34, 56));

        assertThat(responses.get(1).getNotificationId()).isEqualTo(2L);
        assertThat(responses.get(1).getType()).isEqualTo("INVITE");
        assertThat(responses.get(1).getMessage()).isEqualTo("초대자닉네임님이 컬렉션에 초대했습니다.");
        assertThat(responses.get(1).getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 1, 17, 12, 34, 56));

        verify(notificationRepository, times(1)).findByUserId(userId);
    }


    @Test
    @DisplayName("알림 삭제 메서드 테스트 - 성공")
    void deleteNotification_Success() {
        // given
        Long notificationId = 1L;
        Long userId = 100L;

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(bookmarkNotification));
        when(bookmarkNotification.getUser().getId()).thenReturn(userId);

        // when, then (예외 발생 없이 정상 동작 확인)
        assertThatCode(() -> notificationService.deleteNotification(notificationId, userId))
                .doesNotThrowAnyException();

        // delete 메서드가 정확히 1번 호출되었는지 검증
        verify(notificationRepository, times(1)).delete(bookmarkNotification);
    }

    @Test
    @DisplayName("알림 삭제 메서드 테스트 - 실패: 알림이 존재하지 않는 경우")
    void deleteNotification_Fail_NotExist() {
        // given
        Long notificationId = 1L;
        Long userId = 100L;

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // when, then (예외 발생 확인)
        assertThatThrownBy(() -> notificationService.deleteNotification(notificationId, userId))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 알림이 존재하지 않습니다.");

        // delete 메서드가 호출되지 않았는지 검증
        verify(notificationRepository, never()).delete(any());
    }

    @Test
    @DisplayName("알림 삭제 메서드 테스트 - 실패: 권한이 없는 경우")
    void deleteNotification_Fail_NoPermission() {
        // given
        Long notificationId = 1L;
        Long userId = 200L;

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(bookmarkNotification));
        when(bookmarkNotification.getUser().getId()).thenReturn(100L);

        // when, then (예외 발생 확인)
        assertThatThrownBy(() -> notificationService.deleteNotification(notificationId, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 알림에 대한 권한이 없습니다.");

        // delete 메서드가 호출되지 않았는지 검증
        verify(notificationRepository, never()).delete(any());
    }

    @Test
    @DisplayName("모든 알림 삭제 메서드 테스트")
    void deleteAllNotifications() {
        // given
        Long userId = 100L;

        // when
        notificationService.deleteAllNotifications(userId);

        // then
        verify(notificationRepository, times(1)).deleteByUser_Id(userId);
    }
}