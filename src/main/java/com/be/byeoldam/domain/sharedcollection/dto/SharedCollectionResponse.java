package com.be.byeoldam.domain.sharedcollection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
public class SharedCollectionResponse {

    private Long id;
    private String name;

    @JsonProperty("isPersonal")
    private boolean isPersonal;

    public static SharedCollectionResponse of(Long id, String name) {
        return SharedCollectionResponse.builder()
                .id(id)
                .name(name)
                .isPersonal(false)
                .build();
    }
}
