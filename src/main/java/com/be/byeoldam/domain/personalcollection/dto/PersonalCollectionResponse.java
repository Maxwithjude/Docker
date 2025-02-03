package com.be.byeoldam.domain.personalcollection.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PersonalCollectionResponse {

    private Long collectionId;
    private String name;

    public static PersonalCollectionResponse of(Long collectionId, String name) {
        return PersonalCollectionResponse.builder()
                .collectionId(collectionId)
                .name(name)
                .build();
    }
}
