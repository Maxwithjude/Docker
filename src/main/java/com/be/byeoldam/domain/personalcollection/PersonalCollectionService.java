package com.be.byeoldam.domain.personalcollection;

import com.be.byeoldam.domain.personalcollection.dto.PersonalCollectionRequest;
import com.be.byeoldam.domain.personalcollection.dto.PersonalCollectionResponse;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonalCollectionService {

    private final UserRepository userRepository;

    private final PersonalCollectionRepository personalCollectionRepository;

    @Transactional
    public void createPersonalCollection(PersonalCollectionRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        validate(user, request.getName());
        PersonalCollection collection = request.toEntity(user);

        personalCollectionRepository.save(collection);
    }

    // 개인컬렉션 목록 조회
    @Transactional(readOnly = true)
    public List<PersonalCollectionResponse> getPersonalCollections(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));
        return personalCollectionRepository.findByUser(user).stream()
                .map(collection -> PersonalCollectionResponse.of(collection.getId(), collection.getName()))
                .toList();
    }

    // 개인컬렉션 이름 수정
    @Transactional
    public PersonalCollectionResponse updatePersonalCollection(PersonalCollectionRequest request, Long userId, Long collectionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        PersonalCollection collection = personalCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new CustomException("컬렉션을 찾을 수 없습니다."));

        if (!collection.getUser().getId().equals(userId)) {
            throw new CustomException("해당 컬렉션에 대한 권한이 없습니다.");
        }
        String newName = request.getName();
        validate(user, newName);
        collection.updateName(newName);
        return PersonalCollectionResponse.of(collectionId, newName);
    }

    // 개인컬렉션 삭제
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
    // TODO : 나중에 controller에서 @Valid로 입력 길이, null 처리
    private void validate(User user, String name) {
        if (personalCollectionRepository.existsByUserIdAndName(user.getId(), name)) {
            throw new CustomException("같은 이름의 컬렉션이 이미 존재합니다.");
        }
    }
}
