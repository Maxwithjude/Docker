package com.be.byeoldam.domain.sharedcollection.dto;

import lombok.*;

@Getter
@Builder
public class SharedCollectionResponse {

    private Long id;
    private String name;

    public static SharedCollectionResponse of(Long id, String name) {
        return SharedCollectionResponse.builder()
                .id(id)
                .name(name)
                .build();
    }
}
