package com.be.byeoldam.personalcollection;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.config.QuerydslConfig;
import com.be.byeoldam.domain.personalcollection.dto.PersonalCollectionRequest;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import java.util.List;
import java.util.Optional;

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
        PersonalCollectionRequest request = new PersonalCollectionRequest("exCollection");
        PersonalCollection collection = request.toEntity(user);

        // when
        PersonalCollection savedCollection = personalCollectionRepository.save(collection);

        // then
        assertThat(savedCollection).isNotNull();
        assertThat(savedCollection.getId()).isNotNull();
        assertThat(collection.getName()).isNotNull();
        assertThat(savedCollection.getUser()).isEqualTo(user);
    }

    // TODO : 테스트 보완 필요..?
    @Test
    @DisplayName("유저의 collection 목록 조회")
    void findPersonalCollectionTest() {
        // given
        String name1 = "ex11";
        String name2 = "ex22";
        PersonalCollectionRequest request1 = new PersonalCollectionRequest("ex11");
        PersonalCollectionRequest request2 = new PersonalCollectionRequest("ex22");
        PersonalCollection collection1 = request1.toEntity(user);
        PersonalCollection collection2 = request2.toEntity(user);
        personalCollectionRepository.save(collection1);
        personalCollectionRepository.save(collection2);
        // when
        List<PersonalCollection> collections = personalCollectionRepository.findByUserId(user.getId());
        // then
        assertThat(collections).extracting("name").containsExactlyInAnyOrder(name1, name2);
    }

    @Test
    @DisplayName("개인컬렉션 수정 - 이름")
    void updatePersonalCollectionTest() {
        // given
        PersonalCollectionRequest request = new PersonalCollectionRequest("exCollection");
        PersonalCollection collection = request.toEntity(user);
        personalCollectionRepository.save(collection);

        String updatedName = "updateName";

        // when
        PersonalCollection updatedCollection = personalCollectionRepository.findById(collection.getId()).orElseThrow(() -> new IllegalArgumentException("해당 개인컬렉션 없음"));

        updatedCollection.updateName(updatedName);
        personalCollectionRepository.save(collection);

        // then
        assertThat(updatedCollection.getName()).isEqualTo(updatedName);
        assertThat(updatedCollection.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("개인컬렉션 개별 삭제")
    void deletePersonalCollectionTest() {
        // given
        PersonalCollectionRequest request = new PersonalCollectionRequest("exCollection");
        PersonalCollection collection = request.toEntity(user);
        personalCollectionRepository.save(collection);

        // when
        personalCollectionRepository.deleteById(collection.getId());

        // then
        Optional<PersonalCollection> deletedCollection = personalCollectionRepository.findById(collection.getId());
        assertThat(deletedCollection).isEmpty();
    }

    // TODO : 개인 컬렉션 조회

}