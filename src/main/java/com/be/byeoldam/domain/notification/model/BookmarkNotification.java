package com.be.byeoldam.domain.notification.model;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@DiscriminatorValue("BOOKMARK")
public class BookmarkNotification extends Notification {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @Builder
    public BookmarkNotification(User user, String message, String title, Bookmark bookmark) {
        super(user, message, title);
        this.bookmark = bookmark;
    }
}
