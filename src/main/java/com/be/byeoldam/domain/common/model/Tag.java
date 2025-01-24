package com.be.byeoldam.domain.common.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Random;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "reference_count", nullable = false)
    private int referenceCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Color color;

    @PrePersist
    private void prePersist() {
        this.referenceCount = 1;
        this.color = Color.values()[new Random().nextInt(Color.values().length)];
    }

    @Builder
    public Tag(String name) {
        this.name = name;
    }

}
