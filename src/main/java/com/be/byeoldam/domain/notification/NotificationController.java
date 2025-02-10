package com.be.byeoldam.domain.notification;

import com.be.byeoldam.common.ResponseTemplate;
import com.be.byeoldam.common.annotation.UserId;
import com.be.byeoldam.domain.notification.dto.NotificationResponse;
import com.be.byeoldam.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notification", description = "알림 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 사용자의 알림 목록 조회
     */
    @Operation(summary = "알림 목록 조회", description = "사용자의 알림 목록을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "알림 목록 조회 성공",
            useReturnTypeSchema = true
    )
    @GetMapping
    public ResponseTemplate<List<NotificationResponse>> getNotifications(
            @UserId Long userId
    ) {
        return ResponseTemplate.ok(notificationService.getNotifications(userId));
    }

    /**
     * 특정 알림 삭제
     */
    @Operation(summary = "알림 삭제", description = "특정 알림을 삭제합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "알림 삭제 성공"
    )
    @DeleteMapping("/{notificationId}/read")
    public ResponseTemplate<Void> deleteNotification(
            @PathVariable Long notificationId,
            @UserId Long userId
    ) {
        notificationService.deleteNotification(notificationId, userId);
        return ResponseTemplate.ok();
    }

    /**
     * 사용자의 모든 알림 삭제
     */
    @Operation(summary = "모든 알림 삭제", description = "사용자의 모든 알림을 삭제합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "모든 알림 삭제 성공"
    )
    @DeleteMapping("/read")
    public ResponseTemplate<Void> deleteAllNotifications(
            @UserId Long userId
    ) {
        notificationService.deleteAllNotifications(userId);
        return ResponseTemplate.ok();
    }
}
