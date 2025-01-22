package com.be.byeoldam.domain.sharedcollection.model;

import com.be.byeoldam.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="shared_collection")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedCollection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable false
    @Column(nullable = false, length = 10)
    private String name;

    // 하나의 공유 컬렉션은 여러 유저를 가진다.
    // 한명의 유저는 여러 공유 컬렉션을 가진다.
    @OneToMany(mappedBy = "sharedCollection")
    private List<SharedUser> sharedUsers = new ArrayList<>();
}
