package com.be.byeoldam.domain.tag;

import com.be.byeoldam.common.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/recommend")
    public ResponseEntity<ResponseTemplate<List<String>>> recommendTags(){
        List<String> recommendedTags = tagService.getRecommendedTags();
        return ResponseEntity.ok(ResponseTemplate.ok(recommendedTags));
    }

}
