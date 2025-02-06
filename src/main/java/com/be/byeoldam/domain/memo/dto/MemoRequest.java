package com.be.byeoldam.domain.memo.dto;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.memo.model.Memo;
import com.be.byeoldam.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemoRequest {

    private String content;

    public Memo toEntity(User user, Bookmark bookmark) {
        return Memo.builder()
                .bookmark(bookmark)
                .user(user)
                .content(content)
                .build();
    }
}
