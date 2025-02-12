package com.be.byeoldam.domain.bookmark;

import com.be.byeoldam.common.ResponseTemplate;
import com.be.byeoldam.common.annotation.UserId;
import com.be.byeoldam.domain.bookmark.dto.CreateBookmarkRequest;
import com.be.byeoldam.domain.bookmark.dto.MoveBookmarkRequest;
import com.be.byeoldam.domain.bookmark.dto.UpdateBookmarkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name="bookmark", description = "북마크 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 북마크 생성 - 익스텐션
    @Operation(summary = "북마크 생성", description = "익스텐션에서 북마크 추가")
    @ApiResponse(responseCode = "200", description = "북마크 저장 성공", useReturnTypeSchema = true)
    @PostMapping("/extension")
    public ResponseTemplate<Void> createExtensionBookmark(
            @RequestBody CreateBookmarkRequest request,
            @UserId Long userId
    ) {

        bookmarkService.createBookmark(request, userId);
        return ResponseTemplate.ok();
    }

    // 북마크 생성 - 사이트
    @Operation(summary = "북마크 생성", description = "사이트 내에서 북마크 추가")
    @ApiResponse(responseCode = "200", description = "북마크 저장 성공", useReturnTypeSchema = true)
    @PostMapping
    public ResponseTemplate<Void> createBookmark(
            @RequestBody CreateBookmarkRequest request,
            @UserId Long userId
    ) {

        bookmarkService.createBookmark(request, userId);
        return ResponseTemplate.ok();
    }

    // 북마크 수정 - 태그
    @Operation(summary = "북마크 수정", description = "북마크 태그 수정")
    @ApiResponse(responseCode = "200", description = "북마크 수정 성공", useReturnTypeSchema = true)
    @PutMapping("/{bookmarkId}/tags")
    public ResponseTemplate<Void> updateBookmark(
            @RequestBody UpdateBookmarkRequest request,
            @PathVariable Long bookmarkId, @UserId Long userId
    ) {

        bookmarkService.updateBookmark(request, bookmarkId, userId);
        return ResponseTemplate.ok();
    }

    // 북마크 수정 - 중요도
    @Operation(summary = "북마크 수정", description = "북마크 중요도 수정")
    @ApiResponse(responseCode = "200", description = "북마크 수정 성공", useReturnTypeSchema = true)
    @PutMapping("/{bookmarkId}")
    public ResponseTemplate<Void> changeBookmarkPriority(
            @PathVariable Long bookmarkId,
            @UserId Long userId
    ) {

        bookmarkService.changePriority(bookmarkId);
        return ResponseTemplate.ok();
    }

    // 북마크 삭제
    @Operation(summary = "북마크 삭제", description = "북마크 삭제")
    @ApiResponse(responseCode = "200", description = "북마크 삭제 성공", useReturnTypeSchema = true)
    @DeleteMapping("{bookmarkId}")
    public ResponseTemplate<Void> deleteBookmark(
            @PathVariable Long bookmarkId,
            @UserId Long userId
    ) {

        bookmarkService.deleteBookmark(bookmarkId, userId);
        return ResponseTemplate.ok();
    }

    // 북마크 이동/복사
    @Operation(summary = "북마크 이동", description = "북마크 이동/복사")
    @ApiResponse(responseCode = "200", description = "북마크 이동/복사 성공", useReturnTypeSchema = true)
    @PostMapping("{bookmarkId}/move")
    public ResponseTemplate<Void> moveBookmark(
            @RequestBody MoveBookmarkRequest request,
            @UserId Long userId, @PathVariable Long bookmarkId
    ) {

        bookmarkService.moveBookmark(request, userId, bookmarkId);
        return ResponseTemplate.ok();
    }

}
