package com.be.byeoldam.domain.bookmark.dto;

import com.be.byeoldam.domain.tag.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagDto {
    private String tagName;
    private String tagColor; // 태드 색상
    private String tagBolder; // 태그 테두리 색상

    public Tag toEntity() {
        return Tag.createTag(tagName, tagColor, tagBolder);
    }
}
