package com.be.byeoldam.domain.notification;

import com.be.byeoldam.domain.notification.model.Notification;
import com.be.byeoldam.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

        List<Notification> findByUser(User user);

        Optional<Notification> findByIdAndUser(Long notificationId, User user);

        void deleteByUser(User user);

        int countByUserId(Long userId);
}
