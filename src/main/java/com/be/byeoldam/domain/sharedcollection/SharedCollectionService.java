package com.be.byeoldam.domain.sharedcollection;

import com.be.byeoldam.domain.sharedcollection.dto.SharedCollectionRequest;
import com.be.byeoldam.domain.sharedcollection.dto.SharedCollectionResponse;
import com.be.byeoldam.domain.sharedcollection.model.Role;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.sharedcollection.model.SharedUser;
import com.be.byeoldam.domain.sharedcollection.repository.SharedCollectionRepository;
import com.be.byeoldam.domain.sharedcollection.repository.SharedUserRepository;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedCollectionService {

    private final SharedCollectionRepository sharedCollectionRepository;

    private final SharedUserRepository sharedUserRepository;

    private final UserRepository userRepository;

    // 공유컬렉션 생성
    // 예외 1. user_id 확인
    @Transactional
    public void createSharedCollection(SharedCollectionRequest sharedCollectionRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));
        SharedCollection collection = sharedCollectionRequest.toEntity();
        sharedCollectionRepository.save(collection);
        sharedCollectionRepository.flush();

        SharedUser sharedUser = SharedUser.create(user, collection, Role.OWNER);
        sharedUserRepository.save(sharedUser);
    }

    // 공유컬렉션 목록 조회
    @Transactional(readOnly = true)
    public List<SharedCollectionResponse> getSharedCollection(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));

        List<SharedCollection> collections = sharedUserRepository.findByUser(user);

        return collections.stream()
                .map(collection -> SharedCollectionResponse.of(collection.getId(), collection.getName()))
                .collect(Collectors.toList());
    }

    // 공유컬렉션 수정 - 이름
    // 예외 1. 이름을 바꾸려 하는 사용자와 컬렉션의 id가 일치하는지
    @Transactional
    public SharedCollectionResponse updateSharedCollection(SharedCollectionRequest sharedCollectionRequest, Long userId, Long sharedCollectionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));

        SharedCollection collection = sharedCollectionRepository.findById(sharedCollectionId)
                .orElseThrow(() -> new CustomException(""));

        SharedUser sharedUser = sharedUserRepository.findByUserAndSharedCollection(user, collection)
                .orElseThrow(() -> new CustomException(""));

        if (!sharedUser.getUser().getId().equals(userId)) {
            throw new CustomException("");
        }

        if(!sharedUser.getRole().equals(Role.OWNER)) {
            throw new CustomException("");
        }

        collection.updateName(sharedCollectionRequest.getName());
        return SharedCollectionResponse.of(collection.getId(), collection.getName());
    }

    // 공유컬렉션 삭제
    // 방장만 가능
    // 예외 1. 방장이 아닌 경우
    @Transactional
    public void deleteSharedCollection(Long userId, Long collectionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));

        SharedCollection collection = sharedCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new CustomException(""));

        SharedUser sharedUser = sharedUserRepository.findByUserAndSharedCollection(user, collection)
                .orElseThrow(() -> new CustomException(""));

        if (!sharedUser.getRole().equals(Role.OWNER)) {
            throw new CustomException("");
        }
        sharedCollectionRepository.delete(collection);
        sharedUserRepository.deleteByCollection(collection);
    }


    // 공유컬렉션 멤버 관리 - 초대
    // 초대 알림에서 수락을 누르면 이쪽으로 넘어옴
    // 예외 1 : 이미 초대한 멤버를 또 초대하려고 할 때
    @Transactional
    public void inviteNewMember(Long userId, Long collectionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(""));
        SharedCollection collection = sharedCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new CustomException(""));
        List<SharedUser> sharedUsers = sharedUserRepository.findBySharedCollection(collection);
        if(sharedUsers.stream().anyMatch(sharedUser -> !sharedUser.getUser().getId().equals(userId))) {
            throw new CustomException("");
        }
        SharedUser newSharedUser = SharedUser.create(user, collection, Role.MEMBER);
        sharedUserRepository.save(newSharedUser);
    }

    // 공유멀렉션 멤버 관리 - 강퇴
    // 방장만 가능
    // 예외 1 - 방장 아니면
    // 예외 2 - 컬렉션 멤버가 아닌 사용자를 추방하려고 할 때
    @Transactional
    public void ejectMember(Long userId, Long collectionId, Long ejectedUserId) {
        SharedCollection collection = sharedCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new CustomException(""));

        User ownerUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));
        User ejectedUser = userRepository.findById(ejectedUserId)
                .orElseThrow(() -> new CustomException(""));

        SharedUser ownerSharedUser = sharedUserRepository.findByUserAndSharedCollection(ownerUser, collection)
                .orElseThrow(() -> new CustomException(""));

        if (!ownerSharedUser.getRole().equals(Role.OWNER)) {
            throw new CustomException("");
        }

        SharedUser ejectedSharedUser = sharedUserRepository.findByUserAndSharedCollection(ejectedUser, collection)
                .orElseThrow(() -> new CustomException(""));
        if (!ejectedSharedUser.getRole().equals(Role.MEMBER)) {
            throw new CustomException("");
        }

        sharedUserRepository.delete(ejectedSharedUser);
    }
}
