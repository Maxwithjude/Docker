package com.be.byeoldam.tag;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.config.QuerydslConfig;
import com.be.byeoldam.domain.tag.model.Tag;
import com.be.byeoldam.domain.tag.model.UserTag;
import com.be.byeoldam.domain.tag.repository.TagRepository;
import com.be.byeoldam.domain.tag.repository.UserTagRepository;
import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@SpringBootTest
@DataJpaTest
@Import({AuditingConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TagRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserTagRepository userTagRepository;

    @PersistenceContext
    EntityManager entityManager; // EntityManager 주입

    @BeforeEach
    void setup(){
        userTagRepository.deleteAll();
        userRepository.deleteByEmail("test1@example.com"); // 특정 이메일의 유저 삭제
    }

    @Test
    @Rollback
    void testSaveAndFindTag() {
        // Given
        String tagName = "공부";
        Tag tag = Tag.create(tagName);

        // When: Tag 객체를 DB에 저장하고, 저장된 객체를 조회
        Tag savedTag = tagRepository.save(tag);  // DB에 저장
        Tag foundTag = tagRepository.findById(savedTag.getId()).orElse(null);

        // Then: 저장된 Tag와 조회된 Tag가 동일한지 검증
        assertThat(foundTag.getName()).isEqualTo(tagName);  // 이름이 동일해야 한다
        assertThat(foundTag.getReferenceCount()).isEqualTo(0);  // 기본값이 0이어야 한다
        assertThat(foundTag.getColor()).isEqualTo(savedTag.getColor()); // 색상이 null이 아니어야 한다 (랜덤 값)
        String color = foundTag.getColor().toString();
    }


    @Test
    @Transactional
    @Rollback
    void testDecrementReferenceCountByName() {
        // Given: 새로운 Tag 객체를 생성하고 DB에 저장
        String tagName = "공부3";
        Tag tag = Tag.create(tagName);
        tagRepository.save(tag);

        // EntityManager와 QueryDSL이 동기화 되지 않음, 동기화 해주기.
        entityManager.flush();
        entityManager.clear();

        // When: Tag의 referenceCount를 1 감소시킴
        tagRepository.decrementReferenceCountByName(tagName);

        // Then: referenceCount가 1 감소했는지 검증
        Tag updatedTag = tagRepository.findById(tag.getId()).orElse(null);
        assertThat(updatedTag.getReferenceCount()).isEqualTo(-1);
    }

    @Test
    @DisplayName("사용자의 기존 태그를 삭제한 후, 새로운 태그들을 추가하기")
    @Transactional
    @Rollback
    public void 사용자_태그_삭제_후_추가() {
        // Given
        // 유저 저장
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest("test1@example.com", "1234", "testNickname");
        User user = userRegisterRequest.toEntity();
        userRepository.save(user);

        // 태그 저장
        List<String> tagNames = List.of("Java", "Spring", "JPA");
        List<Tag> existingTags = tagNames.stream()
                .map(Tag::create)
                .collect(Collectors.toList());
        tagRepository.saveAll(existingTags);


        // 유저-태그 저장
        List<UserTag> existingUserTags = existingTags.stream()
                .map(tag -> UserTag.create(user, tag))
                .collect(Collectors.toList());
        userTagRepository.saveAll(existingUserTags);

        // When: 기존 태그 삭제 후 새로운 태그 추가
        userTagRepository.deleteByUser(user);
        List<String> newTagNames = List.of("Spring Boot", "Hibernate");

        List<Tag> existingNewTags = tagRepository.findByNameIn(newTagNames);
        Map<String, Tag> existingTagMap = existingNewTags.stream()
                .collect(Collectors.toMap(Tag::getName, tag -> tag));

        List<Tag> newTags = new ArrayList<>();
        for (String tagName : newTagNames) {
            if (!existingTagMap.containsKey(tagName)) {
                Tag newTag = Tag.create(tagName);
                newTags.add(newTag);
            }
        }

        if (!newTags.isEmpty()) {
            tagRepository.saveAll(newTags);
            newTags.forEach(tag -> existingTagMap.put(tag.getName(), tag));
        }

        List<UserTag> newUserTags = existingTagMap.values().stream()
                .map(tag -> UserTag.create(user, tag))
                .collect(Collectors.toList());
        userTagRepository.saveAll(newUserTags);

        // Then: 새로운 태그만 존재해야 함
        List<UserTag> userTagsAfterUpdate = userTagRepository.findByUser(user);
        List<String> storedTagNames = userTagsAfterUpdate.stream()
                .map(userTag -> userTag.getTag().getName())
                .collect(Collectors.toList());

        assertThat(storedTagNames).isEqualTo(newTagNames);
    }

}
