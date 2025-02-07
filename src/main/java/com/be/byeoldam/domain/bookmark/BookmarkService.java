package com.be.byeoldam.domain.bookmark;

import com.be.byeoldam.domain.bookmark.dto.CreateBookmarkRequest;
import com.be.byeoldam.domain.bookmark.dto.MoveBookmarkRequest;
import com.be.byeoldam.domain.bookmark.dto.TagDto;
import com.be.byeoldam.domain.bookmark.dto.UpdateBookmarkRequest;
import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.bookmark.model.BookmarkTag;
import com.be.byeoldam.domain.bookmark.repository.BookmarkRepository;
import com.be.byeoldam.domain.bookmark.repository.BookmarkTagRepository;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.common.repository.BookmarkUrlRepository;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.sharedcollection.repository.SharedCollectionRepository;
import com.be.byeoldam.domain.tag.model.Color;
import com.be.byeoldam.domain.tag.model.Tag;
import com.be.byeoldam.domain.tag.repository.TagRepository;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    private final BookmarkTagRepository bookmarkTagRepository;

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final PersonalCollectionRepository personalCollectionRepository;
    private final SharedCollectionRepository sharedCollectionRepository;
    private final BookmarkUrlRepository bookmarkUrlRepository;

    // 북마크 추가
    // 1. Bookmarks에 추가
    // 2. bookmarkUrl에 추가 or referenceCount increment
    // 3. bookmark_tag에 추가
    // 4. tag 추가 or referenceCount increment
    @Transactional
    public void createBookmark(CreateBookmarkRequest request, Long userId) {
        // user를 찾고
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));

        // 요청을 보낸 user와 userId가 일치하지 않으면
        if (!request.getUserId().equals(userId)) {
            throw new CustomException("");
        }

        // 1. url 저장 과정
        // 1-1. 먼저 request에 url이 잘 있는지 확인
        if (request.getUrl() == null) {
            throw new CustomException("");
        }

        BookmarkUrl bookmarkUrl = null;
        // 1-2. bookmarkUrl에 url이 있는지 확인
        if (bookmarkUrlRepository.existsByUrl(request.getUrl())) {
            // 있으면 referenceCount 올려주기
        } else {
            // 없으면 url 추가해주기
            // TODO : readingTime 나중에 추가 필요
            bookmarkUrl = BookmarkUrl.create(request.getUrl(), 0L, 0);
            bookmarkUrlRepository.save(bookmarkUrl);
        }

        // 2. Bookmark에 bookmark 추가해주기
        // 2-1. 컬렉션 타입 확인하기
        Bookmark bookmark = null;
        if (request.isPersonal()) {
            // 개인컬렉션이면 개인컬렉션 북마크로 만들기
            PersonalCollection collection = personalCollectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new CustomException(""));
            bookmark = Bookmark.createPersonalBookmark(bookmarkUrl, user, collection);
            bookmarkRepository.save(bookmark);
        } else {
            // 공유컬렉션이면 공유컬렉션 북마크로 만들기
            SharedCollection collection = sharedCollectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new CustomException(""));
            bookmark = Bookmark.createSharedBookmark(bookmarkUrl, user, collection);
        }

        // 3. tag에 추가해주기
        // 3-1 request에서 tag 확인하기
        List<TagDto> tagDtos = request.getTags();
        List<Tag> tags = new ArrayList<>();
        // 태그 입력이 있다면
        if (!tagDtos.isEmpty()) {
            // 태그 리스트에서 객체 줄줄이 생성해주기
            tags = tagDtos.stream()
                    .map(dto -> Tag.createTag(
                            dto.getTagName(),
                            Color.fromHex(dto.getTagColor())
                    ))
                    .toList();
            for (Tag tag : tags) {
                if (!tagRepository.existsByName(tag.getName())) {
                    // 새로운 태그면
                    tagRepository.save(tag);
                } else {
                    // tag가 존재한다면
                    // TODO : tag referenceCount 추가
                }
            }
        }

        // 4. bookmark_tag에 추가
        for (Tag tag : tags) {
            BookmarkTag bookmarkTag = BookmarkTag.create(bookmark, tag);
            bookmarkTagRepository.save(bookmarkTag);
        }
    }

    // 북마크 수정
    // 태그 수정 - 수정 완료한 상태의 태그를 요청으로 받음
    //            수정된 태그와 안한 태그를 구분해줘야 함
    // 1. bookmark_tag 추가 or 삭제
    // 2. tag 추가 or referenceCount increment / tag 삭제 or referenceCount decrement
    // 3. bookmarkUrl_tag referenceCount 참고하여 삭제 or 추가
    // queryDSL 쓰는게 좋을,,지도
    @Transactional
    public void updateBookmark(UpdateBookmarkRequest updateBookmarkRequest, Long userId) {

    }

    // 북마크 삭제
    // 1. bookmarks에서 삭제
    // 2. 북마크_태크에서 삭제
    // 3. Tag reference count decrement
    // 4. bookmarkUrl reference count decrement or 삭제
    @Transactional
    public void deleteBookmark(Long userId) {


    }

    // 북마크 이동
    // 이동 : 개인>개인,  복사 : 개인>공유, 공유>개인, 공유>공유
    @Transactional
    public void moveBookmark(MoveBookmarkRequest request, Long userId, Long bookmarkId) {
        // 북마크 찾아놓고
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(""));

        // 유저 찾아놓고
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));

        // 예외 1. 북마크의 userId 와 요청한 userId 일치하지 않으면
        if (!bookmark.getUser().getId().equals(user.getId())) {
            throw new CustomException("");
        }

        // 개인 > 개인: 이동
        if (request.getCollectionId() != null && bookmark.getPersonalCollection() != null) {
            PersonalCollection collection = personalCollectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new CustomException(""));

            bookmark.updatePersonalCollection(collection);
            bookmarkRepository.save(bookmark);
            return;
        }

        // 나머지는 복사 처리
        Bookmark newBookmark = bookmark.copy();

        // 개인 > 공유
        if (bookmark.getPersonalCollection() != null) {
            SharedCollection collection = sharedCollectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new CustomException(""));
            newBookmark.updatePersonalCollection(null); // 개인 컬렉션 해제
            newBookmark.updateSharedCollection(collection);

        // 공유 > 공유
        } else if (bookmark.getSharedCollection() != null) {
            SharedCollection collection = sharedCollectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new CustomException(""));
            newBookmark.updateSharedCollection(collection);

        // 공유 > 개인
        } else {
            PersonalCollection collection = personalCollectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new CustomException(""));
            newBookmark.updateSharedCollection(null); // 공유 컬렉션 해제
            newBookmark.updatePersonalCollection(collection);
        }

        // 복사 북마크 저장
        bookmarkRepository.save(newBookmark);
    }
}
