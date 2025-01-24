package com.be.byeoldam.domain.sharedcollection.model;

import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="shared_collection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedCollection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable false
    @Column(nullable = false, length = 10)
    private String name;

    @OneToMany

    @Builder
    public SharedCollection(User user, String name) {
        this.user = user;
        this.name = name;
    }
}
