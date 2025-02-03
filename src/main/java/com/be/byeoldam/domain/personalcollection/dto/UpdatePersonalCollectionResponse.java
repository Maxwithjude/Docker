package com.be.byeoldam.domain.personalcollection.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdatePersonalCollectionResponse {

    private Long collectionId;
    private String name;
    private LocalDateTime updatedAt;

    public static UpdatePersonalCollectionResponse of(Long collectionId, String name, LocalDateTime updatedAt) {
        return UpdatePersonalCollectionResponse.builder()
                .collectionId(collectionId)
                .name(name)
                .updatedAt(updatedAt)
                .build();
    }

}
