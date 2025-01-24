package com.be.byeoldam.domain.notification.model;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class NotificationTest {

    @Mock
    private User user;

    @Mock
    private Bookmark bookmark;

    @Mock
    private SharedCollection sharedCollection;

    @Test
    @DisplayName("북마크 알림 생성 테스트")
    void createBookmarkNotification() {

        String message = "북마크 알림 테스트";

        BookmarkNotification bookmarkNotification = BookmarkNotification.createNotification(user, message, bookmark);

        assertThat(bookmarkNotification).isNotNull();
        assertThat(bookmarkNotification.getUser()).isEqualTo(user);
        assertThat(bookmarkNotification.getMessage()).isEqualTo(message);
        assertThat(bookmarkNotification.getBookmark()).isEqualTo(bookmark);

    }

    @Test
    @DisplayName("초대 알림 생성 테스트")
    void createInviteNotification() {

        String message = "초대 알림 테스트";
        String nickname = "초대자 닉네임";

        InviteNotification inviteNotification = InviteNotification.createNotification(user, message, sharedCollection, nickname);

        assertThat(inviteNotification).isNotNull();
        assertThat(inviteNotification.getUser()).isEqualTo(user);
        assertThat(inviteNotification.getMessage()).isEqualTo(message);
        assertThat(inviteNotification.getCollection()).isEqualTo(sharedCollection);
        assertThat(inviteNotification.getNickname()).isEqualTo(nickname);
    }
}