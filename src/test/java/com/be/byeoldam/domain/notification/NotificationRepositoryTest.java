package com.be.byeoldam.domain.notification;

import com.be.byeoldam.domain.notification.model.BookmarkNotification;
import com.be.byeoldam.domain.notification.model.InviteNotification;
import com.be.byeoldam.domain.notification.model.Notification;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.be.byeoldam.domain.notification.model.QNotification.notification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationRepositoryTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("findByUserId 메서드 테스트")
    void findByUserId_ShouldReturnNotificationsForUser() {
        // Given
        User user = User.builder()
                .email("test@ssafy.com")
                .nickname("Alice")
                .password("1234")
                .build();
        Notification notification1 = new InviteNotification(user, "You are invited!", null, "Alice");
        Notification notification2 = new BookmarkNotification(user, "7 days passed", null);

        when(notificationRepository.findByUserId(user.getId())).thenReturn(List.of(notification1, notification2));

        // When
        List<Notification> notifications = notificationRepository.findByUserId(user.getId());

        // Then
        assertThat(notifications).hasSize(2); // 알림이 2개여야 함
        assertThat(notifications).extracting("message")
                .containsExactlyInAnyOrder("You are invited!", "7 days passed");
    }
}