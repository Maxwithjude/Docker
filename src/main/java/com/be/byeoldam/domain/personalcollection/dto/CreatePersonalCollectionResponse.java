package com.be.byeoldam.domain.personalcollection.dto;

import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreatePersonalCollectionResponse {

    private Long collectionId;
    private String name;
    private LocalDateTime createdAt;

    public static CreatePersonalCollectionResponse of(Long collectionId, String name, LocalDateTime createdAt) {
        return CreatePersonalCollectionResponse.builder()
                .collectionId(collectionId)
                .name(name)
                .createdAt(createdAt)
                .build();
    }

}
