package com.be.byeoldam.domain.tag.model;

import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
@IdClass(UserTagId.class)
public class UserTag {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private UserTag(User user, Tag tag) {
        this.user = user;
        this.tag = tag;
    }

    public static UserTag create(User user, Tag tag) {
        return new UserTag(user, tag);
    }

}
