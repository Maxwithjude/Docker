package com.be.byeoldam.domain.personalcollection.model;


import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.bookmark.model.Bookmark;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="personal_collection")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalCollection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

// TODO
//    // 1: N에서 N에 해당되는 관계
//    // user_id null 안됨
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id", nullable=false)
//    private User user;

    //not null, varchar(10)
    @Column(name="name", nullable=false, length=10)
    private String name;

    // 북마크와 양방향인게 좋을 것 같음
    @OneToMany(mappedBy = "personalCollection")
    private List<Bookmark> bookmarks = new ArrayList<>();
}
