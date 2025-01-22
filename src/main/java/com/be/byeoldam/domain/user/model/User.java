package com.be.byeoldam.domain.user.model;

import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.user.dto.UserRegisterDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 60, nullable = false, columnDefinition = "ENUM('LOCAL', 'KAKAO', 'NAVER', 'GOOGLE') DEFAULT 'LOCAL'")
    private Provider provider;

    @Column(name = "provider_id", length = 60, nullable = true)
    private String providerId;

    @Column(length = 256, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String nickname;

    @Column(name = "alert_day", nullable = false)
    private Integer alertDay;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @Column(name = "email_verification_token", length = 255, nullable = true)
    private String emailVerificationToken;

    // 이메일 인증
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "access_token", length = 255, nullable = true)
    private String accessToken;

    @Column(name = "refresh_token", length = 255, nullable = true)
    private String refreshToken;

    // 계정 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "is_active", nullable = false, columnDefinition = "ENUM('PENDING', 'ACTIVE', 'INACTIVE', 'LOCKED') DEFAULT 'PENDING'")
    private AccountStatus isActive;

    @Column(name = "profile_url", length = 500, nullable = false)
    private String profileUrl;

    @Builder
    public User(String email, String password, String nickname, Provider provider, Integer alertDay, Boolean isVerified, AccountStatus isActive, String profileUrl, String providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.alertDay = alertDay;
        this.isVerified = isVerified;
        this.isActive = isActive;
        this.profileUrl = profileUrl;
        this.providerId = providerId;
    }

    public static User.UserBuilder fromDto(UserRegisterDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname());
    }

    @PrePersist
    private void prePersist() {
        if (this.provider == null) {
            this.provider = Provider.LOCAL;
        }
        if (this.alertDay == null) {
            this.alertDay = 7;
        }
        if (this.isVerified == null) {
            this.isVerified = true;
        }
        if (this.isActive == null) {
            this.isActive = AccountStatus.ACTIVE;
        }

        // 프로필 이미지를 랜덤하게 배정하는 코드 추후에 넣기.
    }

    public enum AccountStatus {
        PENDING,   // 인증 대기 중
        ACTIVE,    // 인증 완료 및 활성화
        INACTIVE,  // 비활성화된 계정
        LOCKED     // 잠긴 계정
    }
    public enum Provider {
        LOCAL, KAKAO, NAVER, GOOGLE
    }
}
