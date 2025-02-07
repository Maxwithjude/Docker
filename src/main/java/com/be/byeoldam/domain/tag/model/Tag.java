package com.be.byeoldam.domain.tag.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
        this.referenceCount = 0;
        this.color = Color.values()[new Random().nextInt(Color.values().length)];
    }

    public static Tag create(String name){
        return new Tag(name);
    }

    public static Tag createTag(String name, Color color) {
        return new Tag(name, color);
    }

    private Tag(String name){
        this.name = name;
    }

    private Tag(String name, Color color) {
        this.name = name;
        this.color = color;
    }


}
