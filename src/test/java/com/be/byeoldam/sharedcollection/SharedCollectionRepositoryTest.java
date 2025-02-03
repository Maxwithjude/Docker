package com.be.byeoldam.sharedcollection;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.config.QuerydslConfig;
import com.be.byeoldam.domain.sharedcollection.dto.SharedCollectionRequest;
import com.be.byeoldam.domain.sharedcollection.model.Role;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.sharedcollection.model.SharedUser;
import com.be.byeoldam.domain.sharedcollection.repository.SharedCollectionRepository;
import com.be.byeoldam.domain.sharedcollection.repository.SharedUserRepository;
import com.be.byeoldam.domain.user.dto.UserRegisterRequest;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SharedCollectionRepositoryTest {

    @Autowired
    private SharedCollectionRepository sharedCollectionRepository;

    @Autowired
    private SharedUserRepository sharedUserRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;


    @BeforeEach
    void setUp() {
        UserRegisterRequest request = new UserRegisterRequest("test11@example.com", "1234", "testUser");
        user = request.toEntity();
        userRepository.save(user);
    }

    @Test
    @DisplayName("공유컬렉션 생성 테스트")
    void createSharedCollectionTest() {
        // given
        SharedCollection collection = new SharedCollectionRequest("exCollection").toEntity();
        sharedCollectionRepository.save(collection);
        SharedUser sharedUser = SharedUser.builder().user(user).sharedCollection(collection).role(Role.OWNER).build();
        sharedUserRepository.save(sharedUser);

        // when
        Optional<SharedCollection> foundCollection = sharedCollectionRepository.findById(collection.getId());
        Optional<SharedUser> foundSharedUser = sharedUserRepository.findByUserAndSharedCollection(user, collection);

        // then
        assertThat(foundCollection).isPresent();
        assertThat(foundCollection.get().getName()).isEqualTo("exCollection");

        assertThat(foundSharedUser).isPresent();
        assertThat(foundSharedUser.get().getUser()).isEqualTo(user);
        assertThat(foundSharedUser.get().getSharedCollection()).isEqualTo(collection);
        assertThat(foundSharedUser.get().getRole()).isEqualTo(Role.OWNER);
    }
}
