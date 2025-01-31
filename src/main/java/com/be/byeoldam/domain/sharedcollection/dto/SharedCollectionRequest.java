package com.be.byeoldam.domain.sharedcollection.dto;

import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SharedCollectionRequest {

    private String name;

    public SharedCollection toEntity() {
        return SharedCollection.create(name);
    }
}
