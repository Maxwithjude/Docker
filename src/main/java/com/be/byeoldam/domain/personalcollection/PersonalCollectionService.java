package com.be.byeoldam.domain.personalcollection;

import com.be.byeoldam.domain.personalcollection.dto.CreatePersonalCollectionResponse;
import com.be.byeoldam.domain.personalcollection.dto.PersonalCollectionRequest;
import com.be.byeoldam.domain.personalcollection.dto.UpdatePersonalCollectionResponse;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalCollectionService {

    private final UserRepository userRepository;

    private final PersonalCollectionRepository personalCollectionRepository;

    @Transactional
    public CreatePersonalCollectionResponse createPersonalCollection(PersonalCollectionRequest request, Long userId) {
        // 1. 컬렉션을 만들기 이전, 같은 이름으로 만들어진 컬렉션이 있는지 확인 - Q : CustomException??

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        validate(user, request.getName());
        PersonalCollection collection = request.toEntity(user);

        // 2. 개인컬렉션 생성
        personalCollectionRepository.save(collection);
        return CreatePersonalCollectionResponse.of(collection.getId(), collection.getName(), collection.getCreatedAt());
    }

    // 개인컬렉션 목록 조회
    @Transactional(readOnly = true)
    public List<PersonalCollection> getPersonalCollections(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));
        return personalCollectionRepository.findByUser(user);
    }

    // 개인컬렉션 이름 수정
    // 수정하려는 컬렉션의 user_id와 수정하는 유저의 id가 일치하는지 확인 필요
    @Transactional
    public UpdatePersonalCollectionResponse updatePersonalCollection(PersonalCollectionRequest request, Long userId, Long collectionId, String updatedName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        PersonalCollection collection = personalCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new CustomException("컬렉션을 찾을 수 없습니다."));
        if (!collection.getUser().getId().equals(userId)) {
            throw new CustomException("해당 컬렉션에 대한 권한이 없습니다.");
        }

        validate(user, updatedName);
        collection.updateName(updatedName);
        return UpdatePersonalCollectionResponse.of(collectionId, updatedName, collection.getUpdatedAt());
    }

    // 개인컬렉션 삭제 - TODO : Exception : UnAuthorized? AccessDenied?
    @Transactional
    public void deletePersonalCollection(Long userId, Long collectionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        PersonalCollection collection = personalCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new CustomException("컬렉션을 찾을 수 없습니다."));
        if (!collection.getUser().getId().equals(userId)) {
            throw new CustomException("해당 컬렉션에 대한 권한이 없습니다.");
        }
        personalCollectionRepository.delete(collection);
    }

    // 개인컬렉션 이름 검증(create, update)
    private void validate(User user, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException("컬렉션 이름은 필수입니다.");
        }
        if (name.length() > 20) {
            throw new CustomException("컬렉션 이름은 20자 이내여야 합니다.");
        }
        if (personalCollectionRepository.findByUserAndName(user, name).isPresent()) {
            throw new CustomException("같은 이름의 컬렉션이 이미 존재합니다.");
        }
    }
}
