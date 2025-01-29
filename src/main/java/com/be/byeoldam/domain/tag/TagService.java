package com.be.byeoldam.domain.tag;


import com.be.byeoldam.domain.tag.model.Tag;
import com.be.byeoldam.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;

    public List<String> getRecommendedTags() {
        List<Tag> tags = tagRepository.findTop10ByOrderByReferenceCountDesc();
        return tags.stream()
                .map(Tag::getName)  // Tag 엔티티에서 name만 추출
                .collect(Collectors.toList());
    }
}
