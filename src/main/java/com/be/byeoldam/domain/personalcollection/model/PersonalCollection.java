package com.be.byeoldam.domain.personalcollection.model;

import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="personal_collection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalCollection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="name", nullable=false, length=20)
    private String name;

    private PersonalCollection(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public static PersonalCollection create(String name, User user) {
        return new PersonalCollection(name, user);
    }

    public void updateName(String updatedName) {
        this.name = updatedName;
    }
}
