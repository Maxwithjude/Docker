package com.be.byeoldam.domain.notification.model;

import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class InviteNotification extends Notification {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_collection_id", nullable = false)
    private SharedCollection collection;

    private String nickname; // 알림 보낸 사람의 닉네임

    @Builder
    public InviteNotification(User user, String message, SharedCollection collection, String nickname) {
        super(user, message, NotificationType.INVITATION);
        this.collection = collection;
        this.nickname = nickname;
    }
}
