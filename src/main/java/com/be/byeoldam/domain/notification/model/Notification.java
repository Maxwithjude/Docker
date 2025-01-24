package com.be.byeoldam.domain.notification.model;

import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 알림을 받을 유저

    @Column(nullable = false, length = 100)
    private String message;

    protected Notification(User user, String message) {
        this.user = user;
        this.message = message;
    }

}
