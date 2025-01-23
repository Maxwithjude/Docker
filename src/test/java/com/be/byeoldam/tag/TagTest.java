package com.be.byeoldam.tag;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.domain.common.model.Tag;
import com.be.byeoldam.domain.common.repository.tag.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(AuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TagTest {
    @Autowired
    TagRepository tagRepository;

    @Test
    @Rollback(false)
    void testSaveAndFindTag() {
        // Given
        String tagName = "공부";
        Tag tag = Tag.builder()
                .name(tagName)
                .build();

        // When: Tag 객체를 DB에 저장하고, 저장된 객체를 조회
        Tag savedTag = tagRepository.save(tag);  // DB에 저장
        Tag foundTag = tagRepository.findById(savedTag.getId()).orElse(null);

        // Then: 저장된 Tag와 조회된 Tag가 동일한지 검증
        assertThat(foundTag.getName()).isEqualTo(tagName);  // 이름이 동일해야 한다
        assertThat(foundTag.getReferenceCount()).isEqualTo(0);  // 기본값이 0이어야 한다
        assertThat(foundTag.getColor()).isEqualTo(savedTag.getColor()); // 색상이 null이 아니어야 한다 (랜덤 값)
        String color = foundTag.getColor().toString();
        System.out.println("색상:" + color);
    }

}
