package com.be.byeoldam.domain.memo;

import com.be.byeoldam.common.ResponseTemplate;
import com.be.byeoldam.domain.memo.dto.MemoRequest;
import com.be.byeoldam.domain.memo.dto.MemoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Memo", description = "북마크 메모 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks/{bookmarkId}/memos")
public class MemoController {

    private final MemoService memoService;

    // 임시 userId
    private final Long userId = 1L;

    /**
     * 📌 메모 생성
     */
    @Operation(summary = "메모 생성", description = "특정 북마크에 메모를 추가합니다.")
    @ApiResponse(responseCode = "200", description = "메모 생성 성공", useReturnTypeSchema = true)
    @PostMapping
    public ResponseTemplate<MemoResponse> createMemo(
            @PathVariable Long bookmarkId,
            @RequestBody MemoRequest request
    ) {

        return ResponseTemplate.ok(memoService.createMemo(userId, bookmarkId, request));
    }

    /**
     * 📌 메모 목록 조회
     */
    @Operation(summary = "메모 목록 조회", description = "특정 북마크에 속한 메모 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "메모 목록 조회 성공", useReturnTypeSchema = true)
    @GetMapping
    public ResponseTemplate<List<MemoResponse>> getMemoList(
            @PathVariable Long bookmarkId
    ) {

        return ResponseTemplate.ok(memoService.getMemoList(bookmarkId));
    }

    /**
     * 📌 메모 수정
     */
    @Operation(summary = "메모 수정", description = "메모 내용을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "메모 수정 성공", useReturnTypeSchema = true)
    @PutMapping("/{memoId}")
    public ResponseTemplate<MemoResponse> updateMemo(
            @PathVariable Long bookmarkId,
            @PathVariable Long memoId,
            @RequestBody MemoRequest request
    ) {

        return ResponseTemplate.ok(memoService.updateMemo(userId, bookmarkId, memoId, request));
    }

    /**
     * 📌 메모 삭제
     */
    @Operation(summary = "메모 삭제", description = "특정 메모를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "메모 삭제 성공", useReturnTypeSchema = true)
    @DeleteMapping("/{memoId}")
    public ResponseTemplate<Void> deleteMemo(
            @PathVariable Long bookmarkId,
            @PathVariable Long memoId
    ) {

        memoService.deleteMemo(userId, bookmarkId, memoId);
        return ResponseTemplate.ok();
    }
}
