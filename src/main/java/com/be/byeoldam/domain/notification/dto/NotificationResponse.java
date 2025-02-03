package com.be.byeoldam.domain.notification.dto;

import lombok.AllArgsConstructor;
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
    private String url;
    private LocalDateTime createdAt;

    public static NotificationResponse of(Long notificationId, String type, String title, String message, String url, LocalDateTime createdAt) {
        return NotificationResponse.builder()
                .notificationId(notificationId)
                .type(type)
                .title(title)
                .message(message)
                .url(url)
                .createdAt(createdAt)
                .build();
    }

}
