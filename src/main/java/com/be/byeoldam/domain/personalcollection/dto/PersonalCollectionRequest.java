package com.be.byeoldam.domain.personalcollection.dto;

import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.user.model.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PersonalCollectionRequest {

    private String name;

    public PersonalCollection toEntity(User user) {
        return PersonalCollection.createPersonalCollection(name, user);
    }
}