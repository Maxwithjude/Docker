package com.be.byeoldam.domain.bookmark;

import com.be.byeoldam.domain.bookmark.dto.*;
import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.bookmark.model.BookmarkTag;
import com.be.byeoldam.domain.bookmark.repository.BookmarkRepository;
import com.be.byeoldam.domain.bookmark.repository.BookmarkTagRepository;
import com.be.byeoldam.domain.common.model.BookmarkUrl;
import com.be.byeoldam.domain.common.model.TagBookmarkUrl;
import com.be.byeoldam.domain.common.repository.BookmarkUrlRepository;
import com.be.byeoldam.domain.common.repository.TagBookmarkUrlRepository;
import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import com.be.byeoldam.domain.sharedcollection.model.SharedCollection;
import com.be.byeoldam.domain.sharedcollection.repository.SharedCollectionRepository;
import com.be.byeoldam.domain.sharedcollection.repository.SharedUserRepository;
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
    // ❗❗❗ 똑같은 url 저장하려고 하면 막기 (컬렉션)
    // ❗❗❗ 태그 테두리 색상 추가
    @Transactional
    public void createBookmark(CreateBookmarkRequest request, Long userId) {
        // user를 찾고
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(""));

        // 1. url 저장 과정
        // 1-1. 먼저 request에 url이 잘 있는지 확인
        if (request.getUrl() == null) {
            throw new CustomException("");
        }

        // ❗ 개인 컬렉션에 이미 저장된 url이면 저장할 수 없게 처리하기
        // bookmarkUrl이 있으면 그 객체로 personalCollection에
        bookmarkUrlRepository.findByUrl(request.getUrl())
                .flatMap(bookmarkUrl -> bookmarkRepository.findByBookmarkUrlAndUser(bookmarkUrl, user))
                .filter(bookmark -> bookmark.getPersonalCollection() != null)
                .ifPresent(bookmark -> {
                    throw new CustomException("이미 개인 컬렉션에 저장된 북마크입니다.");
                });

        // ❗븍마크 존재 여부 확인 후 없으면 생성, 그 후 +1
        BookmarkUrl bookmarkUrl = bookmarkUrlRepository.findByUrl(request.getUrl())
                .orElseGet(() ->
                    // TODO : readingTime 나중에 추가 필요
                    bookmarkUrlRepository.save(BookmarkUrl.create(request.getUrl(), 0L, 0)));
        bookmarkUrl.increment();

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
        // ❗ 태그의 존재 여부 확인 후 없으면 생성, 그 후 +1
        List<Tag> tags = tagDtos.stream()
                .map(dto -> {
                    Tag tag = tagRepository.findByName(dto.getTagName())
                            .orElseGet(() -> tagRepository.save(Tag.createTag(dto.getTagName(), dto.getTagColor(), dto.getTagBolder())));
                    tag.increment();
                    return tagRepository.save(tag);
                })
                .toList();

        // 4. bookmark_tag에 추가
        // 5. 추가 사항
        // ❗❗❗ tag-bookmarkUrl에도 추가 (빠진 것 같아서 추가함)
        for (Tag tag : tags) {
            bookmarkTagRepository.save(BookmarkTag.create(bookmark, tag));
            if (!tagBookmarkUrlRepository.existsByTagAndBookmarkUrl(tag, bookmarkUrl)) {
                tagBookmarkUrlRepository.save(TagBookmarkUrl.create(tag, bookmarkUrl));
            }
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

        // 기존 태그 리스트
        List<Tag> beforeTags = bookmarkTagRepository.findByBookmark(bookmark).stream()
                .map(BookmarkTag::getTag)
                .toList();

        // 요청에 있는 태그 dto 리스트
        List<TagDto> newTagNames = Optional.ofNullable(request.getTags())
                .orElse(Collections.emptyList());

        // 새롭게 저장되는 태그를 등록, 처음 저장하는 태그면 저장도 해주기
        List<Tag> newTags = newTagNames.stream()
                .filter(tagDto -> tagRepository.findByName(tagDto.getTagName()).isEmpty()) // 🔥 DB에 없는 태그만 필터링
                .map(tagDto -> {
                    Tag newTag = tagDto.toEntity();
                    newTag.increment();
                    return tagRepository.save(newTag);
                })
                .toList();


        // 추가할 태그 북마크-태그에도 추가,
        // ❗❗❗ 태그-북마크링크에서도 추가
        newTags.forEach(tag -> {
            // 북마크-태그 연관관계 추가
            BookmarkTag newBookmarkTag = BookmarkTag.create(bookmark, tag);
            bookmarkTagRepository.save(newBookmarkTag);

            // 북마크링크-태그 연관관계 추가
            tagBookmarkUrlRepository.save(TagBookmarkUrl.create(tag, bookmark.getBookmarkUrl()));
        });


        // 수정 후 없는 태그 리스트 -> 삭제할 태그
        List<Tag> removedTags = beforeTags.stream()
                .filter(tag -> newTags.stream().noneMatch(newTag -> newTag.getName().equals(tag.getName())))
                .toList();

        // 삭제할 태그 referenceCount--, referenceCount 0이면 삭제
        for (Tag tag : removedTags) {
            tag.decrement();
            // 북마크-태그 테이블에서 삭제 (북마크와 태그의 연관관계 삭제)
            bookmarkTagRepository.deleteByBookmarkAndTag(bookmark, tag);
            if (tag.getReferenceCount() == 0) {
                // 태그-url 연관관계 삭제
                tagBookmarkUrlRepository.deleteByTag(tag);
                tagRepository.delete(tag); // 태그 삭제
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
            if (!bookmark.getPersonalCollection().getUser().getId().equals(userId)) {
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
        url.decrement();
        if (url.getReferenceCount() == 0) {
            // 북마크 링크 삭제, tag-bookmarkurl 연관 관계도 삭제하기
            tagBookmarkUrlRepository.deleteByBookmarkUrl(url);
            bookmarkUrlRepository.delete(url);
        }

        // 2. 태그 쪽 삭제
        // 2-1. 북마크-태그 연관관계 삭제
        // 2-2. 태그 referenceCount--, 0이면 태그도 삭제
        List<BookmarkTag> bookmarkTags = bookmarkTagRepository.findByBookmark(bookmark);
        for (BookmarkTag bookmarkTag : bookmarkTags) {
            Tag tag = bookmarkTag.getTag();
            tag.decrement();
            if (bookmarkTag.getTag().getReferenceCount() == 0) {
                // Tag 삭제, 북마크-태그 테이블에서 연관관계 삭제
                bookmarkTagRepository.deleteByTag(tag);
                tagRepository.delete(tag);
            }
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
        if (request.isPersonal() && bookmark.getPersonalCollection() != null) {
            PersonalCollection collection = personalCollectionRepository.findById(request.getCollectionId())
                    .orElseThrow(() -> new CustomException(""));

            bookmark.updatePersonalCollection(collection);
            return; // return : 이동시키고 끝
        }

        // 나머지 경우는 복사 처리
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

        // 복사한 북마크에도 태그를 똑같이 적용하는 기능,,,
        // 4. 기존 북마크의 태그와 북마크 연관 관계 추가
        // 4-1. 태그 referenceCount 추가
        // 기존 북마크의 태그 가져오기
        List<Tag> tags = bookmarkTagRepository.findByBookmark(bookmark)
                .stream()
                .map(BookmarkTag::getTag)
                .toList();
        for (Tag tag : tags) {
            tag.increment(); // 태그의 referenceCount 증가
            // 복사된 북마크와 태그의 관계 추가
            bookmarkTagRepository.save(BookmarkTag.create(newBookmark, tag));
        }

        // 5. 북마크 URL referenceCount 증가
        BookmarkUrl bookmarkUrl = bookmark.getBookmarkUrl();
        bookmarkUrl.increment();
    }

    // 북마크 중요도 수정
    @Transactional
    public void changePriority(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new CustomException(""));

        bookmark.updatePriority();
    }
}