package com.be.byeoldam.personalcollection;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.domain.personalcollection.dto.PersonalCollectionRequest;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
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
@Import(AuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonalCollectionRepositoryTest {

    @Autowired
    private PersonalCollectionRepository personalCollectionRepository;

    @Autowired
    private UserRepository userRepository;

//    UserRegisterRequest userRequest = new UserRegisterRequest("test11@naver.com", "test11", "test11user");
//    User user = userRequest.toEntity();

    @Test
    @DisplayName("컬렉션 생성 테스트")
        // @Rollback(false)
    void savePersonalCollectionTest() {
        // Given
        UserRegisterRequest userRequest = new UserRegisterRequest("test11@naver.com", "test11", "test11user");
        User user1 = userRequest.toEntity();
        User user = userRepository.findById(user1.getId()).get();
        userRepository.save(user);

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

    @Test
    @DisplayName("유저의 collection 목록 조회")
    void findPersonalCollectionTest() {
        // given
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest("ex@example.com", "1234", "testNickname");
        User user1 = userRegisterRequest.toEntity();
        userRepository.save(user1);
        String name1 = "ex11";
        String name2 = "ex22";
        PersonalCollectionRequest request1 = new PersonalCollectionRequest("ex11");
        PersonalCollectionRequest request2 = new PersonalCollectionRequest("ex22");
        PersonalCollection collection1 = request1.toEntity(user1);
        PersonalCollection collection2 = request2.toEntity(user1);
        personalCollectionRepository.save(collection1);
        personalCollectionRepository.save(collection2);
        // when
        List<PersonalCollection> collections = personalCollectionRepository.findByUserId(user1.getId());
        // then
        assertThat(collections).extracting("name").containsExactlyInAnyOrder(name1, name2);
    }

    @Test
    @DisplayName("개인컬렉션 수정 - 이름")
    void updatePersonalCollectionTest() {
        // given
        User user = userRepository.findById(6L).get();
        PersonalCollection collection = personalCollectionRepository.findByUserId(user.getId()).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("collection not found"));
        String updatedName = "updateName";

        // when
        collection = PersonalCollection.builder().user(collection.getUser()).name(updatedName).build();
        PersonalCollection updatedCollection = personalCollectionRepository.save(collection);

        // then
        assertThat(updatedCollection.getName()).isEqualTo(updatedName);
        assertThat(updatedCollection.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("개인컬렉션 개별 삭제")
    void deletePersonalCollectionTest2() {
        User user = userRepository.findById(6L).get();
        personalCollectionRepository.deleteById(2L);
    }
}
