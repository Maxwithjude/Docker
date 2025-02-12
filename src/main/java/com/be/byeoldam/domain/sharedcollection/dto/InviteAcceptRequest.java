package com.be.byeoldam.domain.sharedcollection.dto;

import com.be.byeoldam.domain.notification.model.Notification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InviteAcceptRequest {
    private Long notificationId;
}
