package com.be.byeoldam.domain.sharedcollection.dto;

import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class sharedCollectionRequest {
    private String name;

//    public SharedCollection toEntity() {
////        return SharedCollection
//    }
}
