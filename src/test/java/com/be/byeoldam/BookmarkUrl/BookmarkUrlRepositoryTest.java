package com.be.byeoldam.BookmarkUrl;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.config.QuerydslConfig;
import com.be.byeoldam.domain.common.repository.BookmarkUrlRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({AuditingConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookmarkUrlRepositoryTest {

    private BookmarkUrlRepository bookmarkUrlRepository;

    // 북마크 추가시 일어나야 하는 일
    // 기존에 추가된 적 있는 url인지 확인
    // 북마크 referenceCount++
    // readingTime 추가
    @Test
    @DisplayName("북마크 생성 - Url 추가 : referenceCount 증가")
    void createBookmarkUrl() {

    }

    // 북마크 추가시 일어나야 하는 일
    // 북마크의 referenceCount--
    // 북마크의 referenceCount가 0이 되면
    // delete 해버리기
    @Test
    @DisplayName("북마크 삭제 - Url 삭제 : referenceCount 감소")
    void deleteBookmarkUrl() {

    }
}
