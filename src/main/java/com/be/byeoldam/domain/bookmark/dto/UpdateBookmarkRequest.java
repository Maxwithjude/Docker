package com.be.byeoldam.domain.bookmark.dto;

import com.be.byeoldam.domain.tag.model.Tag;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateBookmarkRequest {

    private List<Tag> tags;

}
