package com.be.byeoldam.tag;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.config.QuerydslConfig;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.common.model.TagBookmarkUrl;
import com.be.byeoldam.domain.common.repository.BookmarkUrlRepository;
import com.be.byeoldam.domain.common.repository.TagBookmarkUrlRepository;
import com.be.byeoldam.domain.tag.dto.RecommendedUrlByTagRequest;
import com.be.byeoldam.domain.tag.dto.RecommendedUrlResponse;
import com.be.byeoldam.domain.tag.model.Tag;
import com.be.byeoldam.domain.tag.repository.TagRepository;
import com.be.byeoldam.domain.tag.util.JsoupUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Import({AuditingConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
class BookmarkUrlRepositoryTest {
    @Autowired
    private TagBookmarkUrlRepository tagBookmarkUrlRepository;

    @Autowired
    private BookmarkUrlRepository bookmarkUrlRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagBookmarkUrlRepository bookmarkUrlTagRepository;

    private RecommendedUrlByTagRequest request;

    @BeforeEach
    void setUp() {
        // 1. URL 저장
        BookmarkUrl url1 = bookmarkUrlRepository.save(new BookmarkUrl("https://www.naver.com/",0L,0));
        BookmarkUrl url2 = bookmarkUrlRepository.save(new BookmarkUrl("https://www.google.com/",0L,0));
        BookmarkUrl url3 = bookmarkUrlRepository.save(new BookmarkUrl("https://www.wikipedia.org",0L,0));
        BookmarkUrl url4 = bookmarkUrlRepository.save(new BookmarkUrl("https://www.facebook.com",0L,0));
        BookmarkUrl url5 = bookmarkUrlRepository.save(new BookmarkUrl("https://www.youtube.com",0L,0));
        BookmarkUrl url6 = bookmarkUrlRepository.save(new BookmarkUrl("https://www.daum.net",0L,0));
        // 2. 태그 저장
        Tag tag = tagRepository.save(Tag.create("검색"));

        // 3. URL-태그 관계 저장
        bookmarkUrlTagRepository.save(TagBookmarkUrl.create(tag,url1));
        bookmarkUrlTagRepository.save(TagBookmarkUrl.create(tag,url2));
        bookmarkUrlTagRepository.save(TagBookmarkUrl.create(tag,url3));
        bookmarkUrlTagRepository.save(TagBookmarkUrl.create(tag,url4));
        bookmarkUrlTagRepository.save(TagBookmarkUrl.create(tag,url5));
        bookmarkUrlTagRepository.save(TagBookmarkUrl.create(tag,url6));

        request = new RecommendedUrlByTagRequest("검색",0L,2);
    }

    @Test
    @DisplayName("태그 기반으로 북마크 URL을 검색하고, 무한 스크롤 방식으로 조회한다.")
    void getBookmarkUrlsByTagNameTest() {
        // When
        List<RecommendedUrlResponse> result = tagBookmarkUrlRepository.findBookmarkUrlIdsByTagName(request.getTagName(), request.getCursorId(), request.getSize())
                .stream()
                .map(JsoupUtil::fetchMetadata)
                .toList();

        // Then
        assertThat(result).hasSize(2); // 2개만 조회해야 함
        System.out.println(result.get(0).getUrl());
        System.out.println(result.get(1).getUrl());
    }
}
