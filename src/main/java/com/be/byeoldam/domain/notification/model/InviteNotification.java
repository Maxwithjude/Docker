package com.be.byeoldam.domain.notification.model;

import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@DiscriminatorValue("INVITE")
public class InviteNotification extends Notification {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_collection_id", nullable = false)
    private SharedCollection collection;

    private String nickname; // 알림 보낸 사람의 닉네임

    private InviteNotification(User user, String message, SharedCollection collection, String nickname) {
        super(user, message);
        this.collection = collection;
        this.nickname = nickname;
    }

    public static InviteNotification createNotification(User user, String message, SharedCollection collection, String nickname) {
        return new InviteNotification(user, message, collection, nickname);
    }
}
