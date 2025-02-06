package com.be.byeoldam.domain.memo.model;


import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Memo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id", nullable = false)
    private Bookmark bookmark;

    @Column(nullable=false)
    private String content;

    @Builder
    public Memo(String content, User user, Bookmark bookmark) {
        this.content = content;
        this.user = user;
        this.bookmark = bookmark;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
