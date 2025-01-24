package com.be.byeoldam.tag;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.config.QuerydslConfig;
import com.be.byeoldam.domain.tag.model.Tag;
import com.be.byeoldam.domain.tag.repository.TagRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@SpringBootTest
@DataJpaTest
@Import({AuditingConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TagTest {
    @Autowired
    TagRepository tagRepository;
    @Autowired
    EntityManager entityManager; // EntityManager 주입

    @Test
    @Rollback
    void testSaveAndFindTag() {
        // Given
        String tagName = "공부";
        Tag tag = Tag.createTag(tagName);

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
        Tag tag = Tag.createTag(tagName);
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

}
