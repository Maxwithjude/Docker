package com.be.byeoldam.domain.common.model;

import com.be.byeoldam.domain.tag.model.Tag;
import com.be.byeoldam.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name="bookmark_url_tag")
@IdClass(TagBookmarkUrlId.class)
public class TagBookmarkUrl {
    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Id
    @ManyToOne
    @JoinColumn(name = "bookmark_url_id")
    private BookmarkUrl bookmarkUrl;


    private TagBookmarkUrl(Tag tag, BookmarkUrl bookmarkUrl) {
        this.tag = tag;
        this.bookmarkUrl = bookmarkUrl;
    }

    public static TagBookmarkUrl create(Tag tag, BookmarkUrl bookmarkUrl) {
        if (tag == null || bookmarkUrl == null) {
            throw new CustomException("태그와 북마크 URL은 반드시 필요합니다.");
        }
        return new TagBookmarkUrl(tag, bookmarkUrl);
    }

}
