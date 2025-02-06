package com.be.byeoldam.domain.sharedcollection.model;

import com.be.byeoldam.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="shared_collection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedCollection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable false, length = 20
    @Column(nullable = false, length = 20)
    private String name;

    private SharedCollection(String name) {
        this.name = name;
    }

    public static SharedCollection create(String name) {
        return new SharedCollection(name);
    }

    public void updateName(String updatedName) {
        this.name = updatedName;
    }

}
