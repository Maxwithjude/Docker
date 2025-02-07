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
import com.be.byeoldam.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private List<User> users;

    String[] names = new String[] {"BangJang", "user1", "user2", "ejected"};
    String[] emails = new String[] {"BangJang@example.com", "user1@example.com", "user2@example.com", "ejected@example.com"};

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            user = new UserRegisterRequest(emails[i], "1234", names[i]).toEntity();
            userRepository.save(user);
            users.add(user);
        }
        user = users.get(0);
    }

    @Test
    @DisplayName("공유컬렉션 생성 테스트")
    void createSharedCollectionTest() {
        // given
        String name = "newCollection";
        SharedCollection collection = new SharedCollectionRequest(name).toEntity();
        sharedCollectionRepository.save(collection);
        SharedUser sharedUser = SharedUser.create(user, collection, Role.OWNER);
        sharedUserRepository.save(sharedUser);

        // when
        Optional<SharedCollection> foundCollection = sharedCollectionRepository.findById(collection.getId());
        Optional<SharedUser> foundSharedUser = sharedUserRepository.findByUserAndSharedCollection(user, collection);

        // then
        assertThat(foundCollection).isPresent();
        assertThat(foundCollection.get().getName()).isEqualTo("newCollection");

        assertThat(foundSharedUser).isPresent();
        assertThat(foundSharedUser.get().getUser()).isEqualTo(user);
        assertThat(foundSharedUser.get().getSharedCollection()).isEqualTo(collection);
        assertThat(foundSharedUser.get().getRole()).isEqualTo(Role.OWNER);
    }

    @Test
    @DisplayName("공유컬렉션 멤버 관리 - 초대")
    void inviteMemberTest() {
        // given - 방 생성
        String name = "newCollection";
        SharedCollection collection = new SharedCollectionRequest(name).toEntity();
        sharedCollectionRepository.save(collection);
        SharedUser sharedUser = SharedUser.create(user, collection, Role.OWNER);
        sharedUserRepository.save(sharedUser);

        // 멤버 초대
        for (int i=1; i<names.length; i++) {
            User invitedUser = users.get(i);
            SharedUser newMember = SharedUser.create(invitedUser, collection, Role.MEMBER);
            sharedUserRepository.save(newMember);
        }

        // when
        List<SharedUser> members = sharedUserRepository.findBySharedCollection(collection);

        // then
        // 초대된 멤버의 이메일 확인
        for (int i = 1; i < members.size(); i++) {
            assertThat(members.get(i).getUser().getEmail()).isEqualTo(emails[i]);
        }

        // 초대된 멤버의 역할 확인
        assertThat(members.get(0).getRole()).isEqualTo(Role.OWNER); // 첫 번째 사용자는 방장
        for (int i = 1; i < members.size(); i++) {
            assertThat(members.get(i).getRole()).isEqualTo(Role.MEMBER); // 나머지는 MEMBER
        }
    }

    @Test
    @DisplayName("유저의 공유컬렉션 목록 조회")
    void getSharedCollectionTest() {
        // given - 방장인 공유 컬렉션
        String name = "newCollection";
        SharedCollection collection = new SharedCollectionRequest(name).toEntity();
        sharedCollectionRepository.save(collection);
        SharedUser sharedUser = SharedUser.create(user, collection, Role.OWNER);
        sharedUserRepository.save(sharedUser);

        // 멤버로 참여하고 있는 공유 컬렉션
        String otherName = "MyCollection";
        User otherUser = users.get(1);
        SharedCollection otherCollection = new SharedCollectionRequest(otherName).toEntity();
        sharedCollectionRepository.save(otherCollection);
        SharedUser otherSharedUser = SharedUser.create(otherUser, otherCollection, Role.OWNER);
        sharedUserRepository.save(otherSharedUser);

        SharedUser memberUser = SharedUser.create(user, otherCollection, Role.MEMBER);
        sharedUserRepository.save(memberUser);

        // when - user가 속한 공유 컬렉션 목록 조회
        List<SharedCollection> userCollections = sharedUserRepository.findByUser(user)
                .stream().map(SharedUser::getSharedCollection).collect(Collectors.toList());
        // then - user는 2개의 공유 컬렉션에 속해야 함 (방장 1개 + 멤버 1개)
        assertThat(userCollections).hasSize(2);
        assertThat(userCollections).extracting("name").containsExactlyInAnyOrder(name, otherName);
    }

    @Test
    @DisplayName("공유컬렉션 수정 - 이름")
    void updateSharedCollectionTest() {
        // given - 방장인 공유 컬렉션
        SharedCollection collection = new SharedCollectionRequest("newCollection").toEntity();
        sharedCollectionRepository.save(collection);
        SharedUser sharedUser = SharedUser.create(user, collection, Role.OWNER);
        sharedUserRepository.save(sharedUser);

        String newName = "updateName";

        // when
        SharedCollection updatedCollection = sharedCollectionRepository.findById(collection.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 컬렉션이 존재하지 않습니다."));

        updatedCollection.updateName(newName);
        sharedCollectionRepository.flush();

        // then
        SharedCollection resultCollection = sharedCollectionRepository.findById(collection.getId())
                .orElseThrow(() -> new CustomException("해당 컬렉션이 존재하지 않습니다."));
        assertThat(resultCollection.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("공유컬렉션 삭제")
    void deleteSharedCollectionTest() {
        // given - user가 방장인 공유컬렉션
        SharedCollection collection = new SharedCollectionRequest("newCollection").toEntity();
        sharedCollectionRepository.save(collection);
        SharedUser sharedUser = SharedUser.create(user, collection, Role.OWNER);
        sharedUserRepository.save(sharedUser);

        // 멤버 유저(otherUser) 추가
        for (int i=1; i<names.length; i++) {
            User invitedUser = users.get(i);
            SharedUser newMember = SharedUser.create(invitedUser, collection, Role.MEMBER);
            sharedUserRepository.save(newMember);
        }

        List<SharedUser> sharedUserBeforeDelete = sharedUserRepository.findBySharedCollection(collection);
        assertThat(sharedUserBeforeDelete).hasSize(names.length);

        // when - sharedUser, sharedCollection에서 삭제할 공유컬렉션 데이터 삭제
        sharedUserRepository.deleteBySharedCollection(collection);
        sharedCollectionRepository.deleteById(collection.getId());

        // then
        List<SharedUser> sharedUserAfterDelete = sharedUserRepository.findBySharedCollection(collection);
        Optional<SharedCollection> deletedCollection = sharedCollectionRepository.findById(collection.getId());

        assertThat(deletedCollection).isEmpty();
        assertThat(deletedCollection).isEmpty();
    }

    @Test
    @DisplayName("공유컬렉션 멤버 관리- 강퇴")
    void ejectMemberTest() {
        // given - user가 방장인 공유컬렉션
        SharedCollection collection = new SharedCollectionRequest("newCollection").toEntity();
        sharedCollectionRepository.save(collection);
        SharedUser sharedUser = SharedUser.create(user, collection, Role.OWNER);
        sharedUserRepository.save(sharedUser);

        // 멤버 유저(otherUser) 추가
        for (int i=1; i<names.length; i++) {
            User invitedUser = users.get(i);
            SharedUser newMember = SharedUser.create(invitedUser, collection, Role.MEMBER);
            sharedUserRepository.save(newMember);
        }

        // 강퇴할 유저
        User ejectedUser = users.get(3);
        SharedUser ejectedSharedUser = sharedUserRepository.findByUserAndSharedCollection(ejectedUser, collection)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버는 컬렉션에 포함되어 있지 않습니다."));

        // 멤버 강퇴 전 멤버 확인
        List<SharedUser> sharedUserBeforeEject = sharedUserRepository.findBySharedCollection(collection);
        assertThat(sharedUserBeforeEject).hasSize(names.length);

        // when - 멤버 강퇴
        sharedUserRepository.delete(ejectedSharedUser);

        // then
        List<SharedUser> sharedUserAfterEject = sharedUserRepository.findBySharedCollection(collection);
        Optional<SharedUser> ejectedUserCheck = sharedUserRepository.findByUserAndSharedCollection(ejectedUser, collection);

        assertThat(sharedUserAfterEject).hasSize(names.length - 1);
        assertThat(ejectedUserCheck).isEmpty();
    }
}
