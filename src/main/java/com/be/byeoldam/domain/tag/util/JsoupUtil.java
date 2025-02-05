package com.be.byeoldam.domain.tag.util;

import com.be.byeoldam.domain.tag.dto.RecommendedUrlResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Optional;

public class JsoupUtil {
    public static RecommendedUrlResponse fetchMetadata(String url) {
        try {
            Document document = Jsoup.connect(url).get();

            String title = document.title();
            String description = Optional.ofNullable(document.selectFirst("meta[name=description]"))
                    .map(meta -> meta.attr("content"))
                    .orElse("No description found");

            String image = Optional.ofNullable(document.selectFirst("meta[property=og:image]"))
                    .map(meta -> meta.attr("content"))
                    .orElse("");

            return RecommendedUrlResponse.of(url,title,description,image);

        } catch (IOException e) {
            e.printStackTrace();
            return RecommendedUrlResponse.of(url,"","","");
        }
    }
}
