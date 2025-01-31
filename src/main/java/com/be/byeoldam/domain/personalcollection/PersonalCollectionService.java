package com.be.byeoldam.domain.personalcollection;

import com.be.byeoldam.domain.personalcollection.dto.PersonalCollectionRequest;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import jakarta.persistence.EntityNotFoundException;
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

        // 1. 컬렉션을 만들기 이전, 같은 이름으로 만들어진 컬렉션이 있는지 확인 - Q : CustomException??
        // TODO : 나중에 user existsBy()로 해달라 말해보기..
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재하지 않습니다. "));
        PersonalCollection collection = request.toEntity(user);
        if (personalCollectionRepository.findByUserIdAndName(userId, collection.getName()).isPresent()) {
            throw new CustomException("같은 이름의 컬렉션이 존재합니다. ");
        }
        // 2. 개인컬렉션 생성
        personalCollectionRepository.save(collection);
    }

    // 개인컬렉션 목록 조회
    @Transactional(readOnly = true)
    public List<PersonalCollection> getPersonalCollections(Long userId) {
        return personalCollectionRepository.findByUserId(userId);
    }

    // 개인컬렉션 이름 수정
    // 수정하려는 컬렉션의 user_id와 수정하는 유저의 id가 일치하는지 확인 필요
    @Transactional
    public PersonalCollection updatePersonalCollection(PersonalCollectionRequest request, Long collectionId, Long userId) {
        PersonalCollection collection = personalCollectionRepository.findById(collectionId).orElseThrow(() -> new EntityNotFoundException("해당 컬렉션이 존재하지 않습니다. "));
        collection.updateName(updatedName);
        return personalCollectionRepository.save(collection);
    }

    // 개인컬렉션 삭제 - TODO : Exception : UnAuthorized? AccessDenied?
    @Transactional
    public void deletePersonalCollection(Long userId, Long collectionId) {
        PersonalCollection collection = personalCollectionRepository.findById(collectionId).orElseThrow(() -> new EntityNotFoundException("해당 컬렉션이 존재하지 않습니다. "));
        if (!collection.getUser().getId().equals(userId)) {
            throw new CustomException("해당 컬렉션을 삭제할 권한이 없습니다. ");
        }
        personalCollectionRepository.deleteById(collectionId);
    }

    // 개인컬렉션 이름 검증(create, update)
    private boolean validate(String name) {
        // 이름 null 아닌지, 길이, 이미 존재하는지 확인하기

    }
}
