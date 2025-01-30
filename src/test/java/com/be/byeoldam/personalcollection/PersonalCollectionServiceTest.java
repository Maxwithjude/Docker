package com.be.byeoldam.personalcollection;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.config.QuerydslConfig;
import com.be.byeoldam.domain.personalcollection.PersonalCollectionService;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({PersonalCollectionService.class, AuditingConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class PersonalCollectionServiceTest {

    @Autowired
    private PersonalCollectionService personalCollectionService;

    @Autowired
    private PersonalCollectionRepository personalCollectionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private PersonalCollection collection;

    @BeforeEach
    void setUp() {
        user = new UserRegisterRequest("test11@example.com", "password", "testUser").toEntity();
        user = userRepository.save(user);

        collection = PersonalCollection.create("exCollection", user);
        collection = personalCollectionRepository.save(collection);
    }

    @Test
    @DisplayName("개인컬렉션 생성 테스트")
    void createPersonalCollectionServiceTest() {
        // when
        PersonalCollection result = personalCollectionService.createPersonalCollection(user.getId(), "newCollection");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("newCollection");
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("개인컬렉션 생성 - 동일 이름이 존재하는 경우 예외 발생")
    void createPersonalCollectionServiceTest_DuplicatedName() {
        // when & then
        assertThatThrownBy(() -> personalCollectionService.createPersonalCollection(user.getId(), "exCollection"))
                .isInstanceOf(CustomException.class)
                .hasMessage("같은 이름의 컬렉션이 존재합니다. ");
    }

    // TODO : 테스트 보완 필요
    @Test
    @DisplayName("개인컬렉션 목록 조회 테스트")
    void getPersonalCollectionsTest() {
        // when
        List<PersonalCollection> collections = personalCollectionService.getPersonalCollections(user.getId());

        // then
        assertThat(collections).isNotEmpty();
        assertThat(collections.get(0).getName()).isEqualTo("exCollection");
    }

    // TODO : 이름 빈값, 글자수 예외 처리
    @Test
    @DisplayName("개인컬렉션 수정 테스트 - 이름")
    void updatePersonalCollectionTest() {
        // given
        String updatedName = "updatedName";

        // when
        PersonalCollection updatedCollection = personalCollectionService.updatePersonalCollection(collection.getId(), updatedName);

        // then
        assertThat(updatedCollection.getName()).isEqualTo(updatedName);
        assertThat(updatedCollection.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("컬렉션 삭제 테스트")
    void deletePersonalCollectionTest() {
        // when
        personalCollectionService.deletePersonalCollection(user.getId(), collection.getId());

        // then
        Optional<PersonalCollection> deletedCollection = personalCollectionRepository.findById(collection.getId());
        assertThat(deletedCollection).isEmpty();
    }

    @Test
    @DisplayName("다른 사용자가 컬렉션을 삭제하려고 하면 예외 발생")
    void deletePersonalCollectionTest_AccessDenied() {
        // given
        User otherUser = userRepository.save(new UserRegisterRequest("other@example.com", "password", "otherUser").toEntity());
        PersonalCollection otherCollection = personalCollectionRepository.save(PersonalCollection.create("OCollection", otherUser));

        // when & then
        assertThatThrownBy(() -> personalCollectionService.deletePersonalCollection(user.getId(), otherCollection.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 컬렉션을 삭제할 권한이 없습니다. ");
    }

    @Test
    @DisplayName("컬렉션을 찾을 수 없을 때 예외 발생")
    void deletePersonalCollection_Fail_NotFound() {
        // when & then
        assertThatThrownBy(() -> personalCollectionService.deletePersonalCollection(user.getId(), 999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 컬렉션이 존재하지 않습니다. ");
    }
}