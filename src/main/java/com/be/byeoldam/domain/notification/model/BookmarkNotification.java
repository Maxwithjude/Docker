package com.be.byeoldam.domain.notification.model;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
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

    private BookmarkNotification(User user, String message, Bookmark bookmark) {
        super(user, message);
        this.bookmark = bookmark;
    }

    public static BookmarkNotification createNotification(User user, String message, Bookmark bookmark) {
        return new BookmarkNotification(user, message, bookmark);
    }

}
