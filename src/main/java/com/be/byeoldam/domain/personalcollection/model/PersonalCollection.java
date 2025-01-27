package com.be.byeoldam.domain.personalcollection.model;


import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="personal_collection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalCollection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1: N에서 N에 해당되는 관계
    // user_id null 안됨
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    //not null, varchar(10)
    @Column(name="name", nullable=false, length=10)
    private String name;

    private PersonalCollection(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public static PersonalCollection createPersonalCollection(String name, User user) {
        return new PersonalCollection(name, user);
    }

}
