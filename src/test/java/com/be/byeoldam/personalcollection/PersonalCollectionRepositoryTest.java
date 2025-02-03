package com.be.byeoldam.personalcollection;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.config.QuerydslConfig;
import com.be.byeoldam.domain.personalcollection.dto.PersonalCollectionRequest;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AuditingConfig.class, QuerydslConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonalCollectionRepositoryTest {

    @Autowired
    private PersonalCollectionRepository personalCollectionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        UserRegisterRequest request = new UserRegisterRequest("test@example.com", "1234", "testUser");
        user = request.toEntity();
        userRepository.save(user);
    }

    @Test
    @DisplayName("컬렉션 생성 테스트")
    // @Rollback(false)
    void savePersonalCollectionTest() {
        // Given
        PersonalCollection collection = new PersonalCollectionRequest("exCollection").toEntity(user);

        // when
        PersonalCollection savedCollection = personalCollectionRepository.save(collection);

        // then
        assertThat(savedCollection).isNotNull();
        assertThat(savedCollection.getId()).isNotNull();
        assertThat(savedCollection.getName()).isEqualTo(collection.getName());
        assertThat(savedCollection.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("유저의 collection 목록 조회")
    void findPersonalCollectionTest() {
        // given
        String name1 = "ex11";
        String name2 = "ex22";
        PersonalCollection collection1 = new PersonalCollectionRequest(name1).toEntity(user);
        PersonalCollection collection2 = new PersonalCollectionRequest(name2).toEntity(user);
        personalCollectionRepository.save(collection1);
        personalCollectionRepository.save(collection2);
        personalCollectionRepository.flush();

        // when
        List<PersonalCollection> collections = personalCollectionRepository.findByUser(user);

        // then
        assertThat(collections).extracting("name").containsExactlyInAnyOrder(name1, name2);
    }

    @Test
    @DisplayName("개인컬렉션 수정 - 이름")
    void updatePersonalCollectionTest() {
        // given
        PersonalCollection collection = new PersonalCollectionRequest("oldCollection").toEntity(user);
        personalCollectionRepository.save(collection);

        String newName = "updateName";

        // when
        PersonalCollection updatedCollection = personalCollectionRepository.findById(collection.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 개인컬렉션이 존재하지 않습니다."));

        updatedCollection.updateName(newName);
        personalCollectionRepository.flush();

        // then
        PersonalCollection resultCollection = personalCollectionRepository.findById(collection.getId())
                .orElseThrow(() -> new CustomException("해당 컬렉션이 존재하지 않습니다."));
        assertThat(resultCollection.getName()).isEqualTo(newName);
        assertThat(resultCollection.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("개인컬렉션 개별 삭제")
    void deletePersonalCollectionTest() {
        // given
        PersonalCollection collection = new PersonalCollectionRequest("exCollection").toEntity(user);
        personalCollectionRepository.save(collection);

        // when
        personalCollectionRepository.deleteById(collection.getId());

        // then
        boolean exists = personalCollectionRepository.existsById(collection.getId());
        assertThat(exists).isFalse();
    }
}