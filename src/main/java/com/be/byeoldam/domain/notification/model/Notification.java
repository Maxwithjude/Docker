package com.be.byeoldam.domain.notification.model;

import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "notification_type")
public abstract class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 알림을 받을 유저

    @Column(nullable = false, length = 100)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Builder
    protected Notification(User user, String message, NotificationType type) {
        this.user = user;
        this.message = message;
        this.type = type;
    }
}
