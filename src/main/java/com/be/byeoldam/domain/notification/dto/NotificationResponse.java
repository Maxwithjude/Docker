package com.be.byeoldam.domain.notification.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private Long notificationId;
    private String type;
    private String title;
    private String message;
    private LocalDateTime createdAt;
}
