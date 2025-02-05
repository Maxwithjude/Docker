package com.be.byeoldam.domain.tag.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UrlPreview {
    private String title;
    private String description;
    private String imageUrl;
}
