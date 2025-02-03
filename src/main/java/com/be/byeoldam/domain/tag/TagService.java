package com.be.byeoldam.domain.tag;


import com.be.byeoldam.domain.common.repository.TagBookmarkUrlRepository;
import com.be.byeoldam.domain.tag.dto.RecommendedUrlResponse;
import com.be.byeoldam.domain.tag.dto.RecommendedUrlByTagRequest;
import com.be.byeoldam.domain.tag.model.Tag;
import com.be.byeoldam.domain.tag.model.UserTag;
import com.be.byeoldam.domain.tag.repository.TagRepository;
import com.be.byeoldam.domain.tag.repository.UserTagRepository;
import com.be.byeoldam.domain.tag.util.JsoupUtil;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;
    private final TagBookmarkUrlRepository tagBookmarkUrlRepository;

    // 참조 횟수가 많은 상위 10개 태그를 조회
    @Transactional(readOnly = true)
    public List<String> getRecommendedTags() {
        List<Tag> tags = tagRepository.findTop10ByOrderByReferenceCountDesc();
        return tags.stream()
                .map(Tag::getName)  // Tag 엔티티에서 name만 추출
                .collect(Collectors.toList());
    }


    // 사용자 정의 태그 조회
    @Transactional(readOnly = true)
    public List<String> getUserTags(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        return userTagRepository.findByUser(user)
                .stream()
                .map(userTag -> userTag.getTag().getName())
                .collect(Collectors.toList());
    }


    // 사용자 정의 태그 삭제 후, 추가하기
    @Transactional
    public void addUserTag(Long userId, List<String> tagList) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다."));

        // 1. 해당 유저의 기존 UserTag 데이터 삭제
        userTagRepository.deleteByUser(user);
        // 2. tagList에 있는 태그들을 한 번에 조회
        List<Tag> existingTagList = tagRepository.findByNameIn(tagList);
        // 3. 이미 존재하는 태그들을 Map으로 변환 (name -> Tag)
        Map<String, Tag> existingTagMap = existingTagList.stream()
                .collect(Collectors.toMap(Tag::getName, tag -> tag));

        List<Tag> newTagList = new ArrayList<>();
        for (String tagName : tagList) {
            if (!existingTagMap.containsKey(tagName)) {
                // 4. 존재하지 않는 태그는 새로 생성
                Tag newTag = Tag.create(tagName);
                newTagList.add(newTag);
            }
        }
        // 5. 새로운 태그들 한 번에 저장
        if (!newTagList.isEmpty()) {
            tagRepository.saveAll(newTagList);
            newTagList.forEach(tag -> existingTagMap.put(tag.getName(), tag)); // 새로 저장된 태그를 Map에 추가
        }
        
        // 6. 모든 태그를 UserTag 테이블에 저장 (중복 체크 없이 덮어쓰기)
        List<UserTag> userTags = existingTagMap.values().stream()
                .map(tag -> UserTag.create(user, tag))
                .collect(Collectors.toList());
        if (!userTags.isEmpty()) {
            userTagRepository.saveAll(userTags);
        }
        
    }


    // 태그 기반 검색(무한 스크롤)
    List<RecommendedUrlResponse> getBookmarkUrlsByTagName(RecommendedUrlByTagRequest request) {
       List<String> urlList = tagBookmarkUrlRepository.findBookmarkUrlIdsByTagName(request.getTagName(),request.getCursorId(), request.getSize());
        return urlList.stream()
                .map(JsoupUtil::fetchMetadata)
                .collect(Collectors.toList());
    }

}
