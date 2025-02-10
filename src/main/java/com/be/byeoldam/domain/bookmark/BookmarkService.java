package com.be.byeoldam.domain.bookmark;

import com.be.byeoldam.domain.bookmark.dto.*;
import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.bookmark.model.BookmarkTag;
import com.be.byeoldam.domain.bookmark.repository.BookmarkRepository;
import com.be.byeoldam.domain.bookmark.repository.BookmarkTagRepository;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.common.repository.BookmarkUrlRepository;
import com.be.byeoldam.domain.common.repository.TagBookmarkUrlRepository;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.sharedcollection.model.SharedUser;
import com.be.byeoldam.domain.sharedcollection.repository.SharedCollectionRepository;
import com.be.byeoldam.domain.sharedcollection.repository.SharedUserRepository;
import com.be.byeoldam.domain.tag.model.Color;
import com.be.byeoldam.domain.tag.model.Tag;
import com.be.byeoldam.domain.tag.repository.TagRepository;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    private final SharedUserRepository sharedUserRepository;
    private final TagBookmarkUrlRepository tagBookmarkUrlRepository;

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

        BookmarkUrl bookmarkUrl = bookmarkUrlRepository.findByUrl(request.getUrl())
                .map(existingBookmark -> {
                    // 있으면 referenceCount 증가
                    existingBookmark.increment();
                    return bookmarkUrlRepository.save(existingBookmark);
                })
                .orElseGet(() ->
                    // 없으면 새 URL 추가
                    // TODO : readingTime 나중에 추가 필요
                    bookmarkUrlRepository.save(BookmarkUrl.create(request.getUrl(), 0L, 0)));

        // 2. Bookmark에 bookmark 추가해주기
        // 2-1. 컬렉션 타입 확인하기
        Bookmark bookmark = null;
        if (request.isPersonal()) {
            // 개인컬렉션이면 개인컬렉션 북마크로 만들기
            PersonalCollection collection = personalCollectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new CustomException(""));
            bookmark = Bookmark.createPersonalBookmark(bookmarkUrl, user, collection);
        } else {
            // 공유컬렉션이면 공유컬렉션 북마크로 만들기
            SharedCollection collection = sharedCollectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new CustomException(""));
            bookmark = Bookmark.createSharedBookmark(bookmarkUrl, user, collection);
        }
        bookmarkRepository.save(bookmark);

        // 3. tag에 추가해주기
        // 3-1 request에서 tag 확인하기
        List<TagDto> tagDtos = Optional.ofNullable(request.getTags())
                .orElse(Collections.emptyList());

        // 3-1. 태그 리스트 저장 or referenceCount++
        List<Tag> tags = tagDtos.stream()
                .map(dto -> tagRepository.findByName(dto.getTagName())
                        .map(existingTag -> { // 기존에 있는 태그 referenceCount++
                            existingTag.increment();
                            return tagRepository.save(existingTag);
                        })
                        .orElseGet(() -> tagRepository.save(Tag.createTag(dto.getTagName(), Color.fromHex(dto.getTagColor()))))) // 없으면 새로 생성
                .toList();

        // 4. bookmark_tag에 추가
        for (Tag tag : tags) {
            bookmarkTagRepository.save(BookmarkTag.create(bookmark, tag));
        }
    }

    // 북마크 수정
    // 태그 수정 - 수정 완료한 상태의 태그를 요청으로 받음
    //            수정된 태그와 안한 태그를 구분해줘야 함
    // 1. bookmark_tag 추가 or 삭제
    // 2. tag 추가 or referenceCount increment / tag 삭제 or referenceCount decrement
    // 3. bookmarkUrl_tag referenceCount 참고하여 삭제 or 추가
    @Transactional
    public void updateBookmark(UpdateBookmarkRequest request, Long userId, Long bookmarkId) {
        // user 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));

        // 북마크 가져오기
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(""));

        // 수정 전 북마크 태그 불러오기
        List<BookmarkTag> bookmarkTags = bookmarkTagRepository.findByBookmark(bookmark);

        // 기존 태그 리스트
        List<Tag> beforeTags = bookmarkTags.stream()
                .map(BookmarkTag::getTag) // BookmarkTag에서 Tag 객체 추출
                .toList(); // List<Tag> 생성

        // 요청에 있는 태그 이름 리스트
        List<String> newTagNames = Optional.ofNullable(request.getTags())
                .orElse(Collections.emptyList());

        List<Tag> newTags = newTagNames.stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(Tag.create(name))))
                .toList();

        // 추가할 태그 북마크-태그에도 추가
        newTags.stream()
                .filter(tag -> !beforeTags.contains(tag))
                .forEach(tag -> {
                    BookmarkTag newBookmarkTag = BookmarkTag.create(bookmark, tag);
                    bookmarkTagRepository.save(newBookmarkTag);
                });

        // 기존에 있었으나 요청에 없는 태그 리스트 -> 삭제해야 함
        List<Tag> removedTags = beforeTags.stream()
                .filter(tag -> !newTagNames.contains(tag.getName()))
                .toList();

        // 삭제할 태그 referenceCount--, 0이면 삭제
        for (Tag tag : removedTags) {
            // 북마크-태그 테이블에서 삭제
            bookmarkTagRepository.deleteByBookmarkAndTag(bookmark, tag);
            if (tag.getReferenceCount() == 1) {
                tagBookmarkUrlRepository.deleteByTag(tag);
                tagRepository.delete(tag); // 태그 삭제
            } else {
                tagRepository.save(tag); // referenceCount가 0이 아니면 저장
            }
        }
    }

    // 북마크 삭제
    // 1. bookmarks에서 삭제
    // 2. 북마크_태크에서 삭제
    // 3. Tag reference count decrement
    // 4. bookmarkUrl reference count decrement or 삭제
    @Transactional
    public void deleteBookmark(Long userId, Long bookmarkId) {
        // user를 찾고
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));

        // 북마크를 찾아오기
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(""));

        // 북마크를 삭제할 수 있는 유저인지 확인
        // 개인컬렉션이라면 user로 바로 판별 가능하지만,
        // 공유컬렉션이라면 같은 컬렉션에 있는 유저인지 확인해봐야함
        if (bookmark.getPersonalCollection() != null) {
            // 개인컬렉션에 있는 북마크
            if (!bookmark.getPersonalCollection().getUser().equals(userId)) {
                throw new CustomException("");
            }
        } else {
            // 공유컬렉션에 있는 북마크
            // 삭제하려는 북마크의 유저와 같은 공유컬렉션에 속하는지 확인
            if (!sharedUserRepository.existsByUserAndSharedCollection(user, bookmark.getSharedCollection())) {
                throw new CustomException("");
            }
        }

        // 1. 북마크링크 referenceCount-- or 삭제
        // 삭제 시 북마크링크_태그도 지워줘야 함
        BookmarkUrl url = bookmarkUrlRepository.findById(bookmark.getBookmarkUrl().getId())
                .orElseThrow(() -> new CustomException(""));
        if (url.getReferenceCount() == 1) {
            // referenceCount 감소 시 0 -> bookmarkUrl 삭제
            // 북마크링크_태그도 삭제
            tagBookmarkUrlRepository.deleteByUrl(url);
            bookmarkUrlRepository.delete(url);
        } else {
            url.decrement();
            bookmarkUrlRepository.save(url);
        }

        // 2. 태그 referenceCount-- or 삭제
        // 삭제 시 북마크_태그 테이블에서도 삭제해줘야 함
        List<BookmarkTag> bookmarkTags = bookmarkTagRepository.findByBookmark(bookmark);
        for (BookmarkTag bookmarkTag : bookmarkTags) {
            Tag tag = bookmarkTag.getTag();
            if (bookmarkTag.getTag().getReferenceCount() == 1) {
                // referenceCount 감소 시 0 -> Tag 삭제, 북마크-태그 테이블에서 연관관계 삭제
                tagBookmarkUrlRepository.deleteByTag(tag);
                tagRepository.delete(tag);
            } else {
                tag.decrement();
                tagRepository.save(tag);
            }
            // 북마크-태그에서 삭제할 북마크 연관 있는거 다 삭제
            bookmarkTagRepository.deleteByBookmark(bookmark);
        }

        // 삭제가 가능한 유저라면,,, 삭제를 해야 함
        bookmarkRepository.delete(bookmark);
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

        // 복사한 북마크에도 태그를 똑같이 적용한다면
        // 4. 북마크 태그 복사 및 태그 referenceCount 증가
        List<BookmarkTag> bookmarkTags = bookmarkTagRepository.findByBookmark(bookmark);
        for (BookmarkTag bookmarkTag : bookmarkTags) {
            Tag tag = bookmarkTag.getTag();
            tag.increment(); // 태그의 referenceCount 증가
            tagRepository.save(tag);

            // 복사된 북마크와 태그의 관계 추가
            bookmarkTagRepository.save(BookmarkTag.create(newBookmark, tag));
        }

        // 5. 북마크 URL referenceCount 증가
        BookmarkUrl bookmarkUrl = bookmark.getBookmarkUrl();
        bookmarkUrl.increment();
        bookmarkUrlRepository.save(bookmarkUrl);
    }

    // 북마크 중요도 수정
    @Transactional
    public void changePriority(ChangePriorityRequest request, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(""));

        bookmark.updatePriority(request.getPriority());
        bookmarkRepository.save(bookmark);
    }

    // 북마크 상세 조회 - url만 전달하면 될듯
    @Transactional
    public BookmarkUrlResponse getBookmarkUrl(Long userId, Long bookmarkId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));
        // 북마크를 찾고
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(""));
        // 북마크에 저장되어 있는 url의 id로 url을 찾기
        BookmarkUrl bookmarkUrl = bookmarkUrlRepository.findById(bookmark.getBookmarkUrl().getId())
                .orElseThrow(() -> new CustomException(""));
        return new BookmarkUrlResponse(bookmarkUrl.getUrl());
    }
}
