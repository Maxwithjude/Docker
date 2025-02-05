package com.be.byeoldam.domain.common.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor //@IdClass를 사용 시, 필요
@EqualsAndHashCode
public class TagBookmarkUrlId implements Serializable {
    private Long tag;
    private Long bookmarkUrl;
}
