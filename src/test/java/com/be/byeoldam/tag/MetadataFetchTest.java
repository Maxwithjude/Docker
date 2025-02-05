package com.be.byeoldam.tag;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MetadataFetchTest {

    @Test
    void testFetchMetadata() throws IOException {
        // 테스트할 URL
        String url = "https://www.naver.com/";

        // Jsoup을 사용해 HTML 문서 가져오기
        Document document = Jsoup.connect(url).get();
        Map<String, String> metadata = new HashMap<>();

        // 제목 추출
        metadata.put("title", document.title());

        // 메타 설명 추출
        metadata.put("description", Optional.ofNullable(document.selectFirst("meta[name=description]"))
                .map(meta -> meta.attr("content"))
                .orElse("No description found"));

        // 메타 키워드 추출
        metadata.put("keywords", Optional.ofNullable(document.selectFirst("meta[name=keywords]"))
                .map(meta -> meta.attr("content"))
                .orElse("No keywords found"));

        // 대표 이미지 추출 (og:image 사용)
        metadata.put("image", Optional.ofNullable(document.selectFirst("meta[property=og:image]"))
                .map(meta -> meta.attr("content"))
                .orElse(
                        Optional.ofNullable(document.selectFirst("img"))
                                .map(img -> img.attr("src"))
                                .orElse("")
                ));
    }
}