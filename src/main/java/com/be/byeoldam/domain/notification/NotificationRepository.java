package com.be.byeoldam.domain.notification;

import com.be.byeoldam.domain.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * 유저의 알림 목록 조회
     * SELECT n
     * FROM Notification n
     * WHERE n.user.id = :userId
     */
    List<Notification> findByUserId(Long userId);
}
