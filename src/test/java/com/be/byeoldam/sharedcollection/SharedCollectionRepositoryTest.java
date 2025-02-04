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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AuditingConfig.class, QuerydslConfig.class})
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
        String[] names = new String[] {"BangJang", "user1", "user2", "Ejected"};
        for (String n : names) {
            User user = new UserRegisterRequest(n, "1234", "testUser").toEntity();
            userRepository.save(user);
        }
    }

    @Test
    @DisplayName("공유컬렉션 생성 테스트")
    void createSharedCollectionTest() {
        // given
        String name = "newCollection";
        SharedCollection collection = new SharedCollectionRequest(name).toEntity();
        sharedCollectionRepository.save(collection);
        System.out.println(user.getId());
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

    @Test
    @DisplayName("유저의 공유컬렉션 목록 조회")
    void getSharedCollectionTest() {
    }

    @Test
    @DisplayName("공유컬렉션 수정 - 이름")
    void updateSharedCollectionTest() {

    }

    @Test
    @DisplayName("공유컬렉션 삭제")
    void deleteSharedCollectionTest() {

    }

    @Test
    @DisplayName("공유컬렉션 멤버 관리 - 초대")
    void inviteMemberTest() {

    }

    @Test
    @DisplayName("공유컬렉션 멤버 관리- 강퇴")
    void ejectMemberTest() {

    }

//    공유컬렉션 기능
//1. 공유컬렉션 생성 : 생성한 사람이 방장
//2. 공유컬렉션 수정 : 방장만 컬렉션 이름 수정 가능
//3. 공유컬렉션 목록 조회 : 사용자가 속한 공유컬렉션 목록 조회
//4. 공유컬렉션 멤버 조회 : 공유컬렉션의 멤버 조회
//5. 멤버 관리 기능
//    5-1. 초대 : 멤버 모두 초대 가능, email 입력해서 초대
//.   5-2. 강퇴 : 방장만 사용자 강퇴 가능
//6. 공유 컬렉션 삭제 : 방장만 공유컬렉션 삭제 가능
}
