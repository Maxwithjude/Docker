package com.be.byeoldam.domain.bookmark.model;

import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="bookmarks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 북마크와 북마크 링크 관계에서 북마크는 N(1:N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id", nullable = false)
    private BookmarkUrl bookmarkUrl;

    // 컬렉션은 여러 북마크를 가지고
    // 북마크는 하나의 컬렉션을 가짐
    // 북마크가 N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="personal_collection_id")
    private PersonalCollection personalcollection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shared_collection_id")
    private SharedCollection sharedCollection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="priority", nullable=false)
    private boolean priority;

    @Column(name="is_read")
    private boolean isRead;

    @Builder
    public Bookmark(BookmarkUrl bookmarkUrl, PersonalCollection personalcollection, SharedCollection sharedCollection, User user, boolean priority, boolean isRead) {
        this.bookmarkUrl = bookmarkUrl;
        this.personalcollection = personalcollection;
        this.sharedCollection = sharedCollection;
        this.user = user;
        this.priority = priority;
        this.isRead = isRead;
    }
}
