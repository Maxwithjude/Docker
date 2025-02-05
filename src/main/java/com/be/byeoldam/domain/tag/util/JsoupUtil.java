package com.be.byeoldam.domain.tag.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Optional;

public class JsoupUtil {
    public static UrlPreview fetchMetadata(String url) {
        String title="";
        String description="No description found";
        String imageUrl="";
        try {
            Document document = Jsoup.connect(url).
                    timeout(5000).
                    get();

            title = document.title();
            description = Optional.ofNullable(document.selectFirst("meta[name=description]"))
                    .map(meta -> meta.attr("content"))
                    .orElse("No description found");

            imageUrl = Optional.ofNullable(document.selectFirst("meta[property=og:image]"))
                    .map(meta -> meta.attr("content"))
                    .orElse("");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return UrlPreview.builder()
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .build();
    }
}
