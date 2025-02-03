    package com.be.byeoldam.domain.notification;

    import com.be.byeoldam.config.AuditingConfig;
    import com.be.byeoldam.config.QuerydslConfig;
    import com.be.byeoldam.domain.notification.model.BookmarkNotification;
    import com.be.byeoldam.domain.notification.model.InviteNotification;
    import com.be.byeoldam.domain.notification.model.Notification;
    import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
    import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
    import com.be.byeoldam.domain.user.model.User;
    import com.be.byeoldam.domain.user.repository.UserRepository;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
    import org.springframework.context.annotation.Import;

    import java.util.List;

    import static org.assertj.core.api.Assertions.assertThat;


    @DataJpaTest
    @Import({AuditingConfig.class, QuerydslConfig.class})
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    class NotificationRepositoryTest {

        @Autowired
        private NotificationRepository notificationRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private TestEntityManager entityManager;

        private User user;

        @BeforeEach
        void setUp() {

            UserRegisterRequest request = new UserRegisterRequest("test@example.com", "1234", "testUser");
            user = request.toEntity();
            userRepository.save(user);

            SharedCollection sharedCollection = SharedCollection.createSharedCollection("컬렉션 이름");
            entityManager.persist(sharedCollection);  // 컬렉션을 먼저 저장
            entityManager.flush();

            InviteNotification inviteNotification = new InviteNotification(user, "You are invited!", sharedCollection, "Alice");
            entityManager.persist(inviteNotification);

            BookmarkNotification bookmarkNotification = new BookmarkNotification(user, "7 days passed", null);
            entityManager.persist(bookmarkNotification);

            // 영속성 컨텍스트 반영
            entityManager.flush();
            entityManager.clear();
        }

        @Test
        @DisplayName("findByUser 메서드 테스트")
        void findByUser_ShouldReturnNotificationsForUser() {
            // When
            List<Notification> notifications = notificationRepository.findByUser(user);

            // Then
            assertThat(notifications).hasSize(2); // 알림이 2개여야 함
            assertThat(notifications).extracting("message")
                    .containsExactlyInAnyOrder("You are invited!", "7 days passed");
        }
    }