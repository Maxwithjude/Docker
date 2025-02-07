package com.be.byeoldam.Bookmark;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.config.QuerydslConfig;
import com.be.byeoldam.domain.bookmark.dto.CreateBookmarkRequest;
import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.common.repository.BookmarkUrlRepository;
import com.be.byeoldam.domain.personalcollection.dto.PersonalCollectionRequest;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.sharedcollection.dto.SharedCollectionRequest;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.sharedcollection.repository.SharedCollectionRepository;
import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({AuditingConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookmarkRepositoryTest {

    private Bookmark bookmark;

    private UserRepository userRepository;

    private PersonalCollectionRepository personalCollectionRepository;

    private SharedCollectionRepository sharedCollectionRepository;

    @BeforeEach
    void setUp() {
//        // 유저 생성
//        User user = new UserRegisterRequest("test11@example.com", "12345", "testUser").toEntity();
//        userRepository.save(user);
//        // 컬렉션 생성
//        PersonalCollection personalCollection = new PersonalCollectionRequest("personal").toEntity(user);
//        personalCollectionRepository.save(personalCollection);
//        SharedCollection sharedCollection = new SharedCollectionRequest("shared").toEntity();
//        sharedCollectionRepository.save(sharedCollection);
//        BookmarkUrl bookmarkUrl = new BookmarkUrl("www.example.com", 0, 3);
//        Bookmark bookmark = new Bookmark(bookmarkUrl, personalCollection, null, user, true, false);
    }

    @Test
    @DisplayName("북마크 생성")
    void createBookmark() {
        // 북마크 request를 받아,,, request를 찢어,,, Bookmark랑 BookmarkUrl 엔티티 객체 만들고,,,


    }

    @Test
    @DisplayName("북마크 조회 - 읽음처리 필요")
    void readBookmark() {

    }

    @Test
    @DisplayName("북마크 중요도 수정")
    void updateBookmarkPriority() {

    }

    @Test
    @DisplayName("북마크 삭제")
    void deleteBookmark() {

    }

    @Test
    @DisplayName("북마크 이동 : 개인->개인")
    void moveBookmark() {

    }

    @Test
    @DisplayName("북마크 복사 : 1.개인>공유, 2.공유>공유,  3.공유>개인")
    void copyBookmark() {

    }
}
