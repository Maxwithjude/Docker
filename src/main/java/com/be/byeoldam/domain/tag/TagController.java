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
    public ResponseTemplate<List<String>> recommendTags(){
        List<String> recommendedTags = tagService.getRecommendedTags();
        return ResponseTemplate.ok(recommendedTags);
    }

    /*추천 북마크가 첫 요청인 경우는 request dto에 name을 null로 요청을 들어오게 해서,
    request.getTagName이 null인 경우는 관심사 태그의 가장 첫번째 name을 적용하는 걸로
    */
}
