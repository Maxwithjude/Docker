package com.be.byeoldam.domain.notification;

import com.be.byeoldam.domain.notification.dto.NotificationResponse;
import com.be.byeoldam.domain.notification.model.BookmarkNotification;
import com.be.byeoldam.domain.notification.model.InviteNotification;
import com.be.byeoldam.domain.notification.model.Notification;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * 사용자의 알림 목록을 조회하는 메서드
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        List<Notification> notifications = notificationRepository.findByUser(user);

        return notifications.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 사용자의 알림을 삭제하는 메서드
     * @param notificationId
     * @param userId
     */
    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        Notification notification = notificationRepository.findByIdAndUser(notificationId, user)
                .orElseThrow(() -> new CustomException("해당 알림이 존재하지 않습니다."));

        if (!notification.getUser().getId().equals(userId)) {
            throw new CustomException("해당 알림에 대한 권한이 없습니다.");
        }

        notificationRepository.delete(notification);
    }

    /**
     * 사용자의 모든 알림을 삭제하는 메서드
     * @param userId
     */
    @Transactional
    public void deleteAllNotifications(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        if (!user.getId().equals(userId)) {
            throw new CustomException("해당 알림에 대한 권한이 없습니다.");
        }

        notificationRepository.deleteByUser(user);
    }

    /**
     * Notification 객체를 NotificationResponse로 변환하는 메서드
     * @param notification
     * @return
     */
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
            url = bookmarkNotification.getBookmark().getBookmarkUrl().getUrl();
            message = bookmarkNotification.getMessage();
            title = getTitleFromUrl(url);
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
