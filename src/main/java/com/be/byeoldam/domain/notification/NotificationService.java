package com.be.byeoldam.domain.notification;

import com.be.byeoldam.domain.notification.dto.NotificationResponse;
import com.be.byeoldam.domain.notification.model.BookmarkNotification;
import com.be.byeoldam.domain.notification.model.InviteNotification;
import com.be.byeoldam.domain.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);

        return notifications.stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationResponse toResponse(Notification notification) {

        String title = "";
        String message = "";
        String url = null;

        if (notification instanceof InviteNotification) { // 초대 알림일 경우
            InviteNotification inviteNotification = (InviteNotification) notification;
            title = inviteNotification.getCollection().getName();
            message = inviteNotification.getNickname() + "님이 컬렉션에 초대했습니다.";
            // 초대 알림은 url이 없음 (null)
        } else if (notification instanceof BookmarkNotification) { // 북마크 알림일 경우
            BookmarkNotification bookmarkNotification = (BookmarkNotification) notification;
            title = bookmarkNotification.getBookmark().getBookmarkUrl().getUrl();
            message = bookmarkNotification.getMessage();
            url = getTitleFromUrl(title);
        }


        return NotificationResponse.of(
                notification.getId(),
                notification.getType(),
                title,
                message,
                url,
                notification.getCreatedAt()
        );
    }

    //todo: url을 통해 title을 가져오는 로직
    private String getTitleFromUrl(String url) {
        return url;
    }
}
