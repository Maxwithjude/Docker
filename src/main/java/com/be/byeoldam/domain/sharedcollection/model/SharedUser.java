package com.be.byeoldam.domain.sharedcollection.model;

import com.be.byeoldam.common.entity.BaseTimeEntity;
import com.be.byeoldam.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shared_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedUser extends BaseTimeEntity { // 공유컬렉션-유저 매핑

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

// TODO
//    // user fk
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id", nullable=false)
//    private User user;

    // 공유컬렉션 fk
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_collection_id", nullable = false)
    private SharedCollection sharedCollection;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
}
