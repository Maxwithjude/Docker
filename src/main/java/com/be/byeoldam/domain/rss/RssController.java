package com.be.byeoldam.domain.rss;


import com.be.byeoldam.common.ResponseTemplate;
import com.be.byeoldam.domain.rss.dto.RssLatestPostsResponse;
import com.be.byeoldam.domain.rss.dto.RssSubscribeRequest;
import com.be.byeoldam.domain.rss.dto.UserRssResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "RSS", description = "RSS API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class RssController {

    private final RssService rssService;

    // 임시 userId
    private final Long userId = 1L;

        /**
         * 사용자의 RSS 구독 목록 조회
         */
        @Operation(summary = "RSS 구독 목록 조회", description = "사용자의 RSS 구독 목록을 조회합니다.")
        @ApiResponse(
                responseCode = "200",
                description = "RSS 구독 목록 조회 성공",
                useReturnTypeSchema = true
        )
        @GetMapping
        public ResponseTemplate<List<UserRssResponse>> getUserRssList(
//                @RequestParam Long userId
        ) {
            return ResponseTemplate.ok(rssService.getUserRssList(userId));
        }

//        /**
//         * 특정 RSS 최신 글 목록 조회
//         */
//        @Operation(summary = "특정 RSS 최신 글 조회", description = "사용자가 구독한 특정 RSS의 최신 글 목록을 조회합니다.")
//        @ApiResponse(
//                responseCode = "200",
//                description = "RSS 최신 글 조회 성공",
//                useReturnTypeSchema = true
//        )
//        @GetMapping("/{rssId}/latest")
//        public ResponseTemplate<RssLatestPostsResponse> getRssLatestArticles(
////                @RequestParam Long userId,
//                @PathVariable Long rssId,
//                Pageable pageable
//        ) {
//            return ResponseTemplate.ok(rssService.getRssLatestArticles(userId, rssId, pageable));
//        }

        /**
         * RSS 구독
         */
        @Operation(summary = "RSS 구독", description = "사용자가 새로운 RSS를 구독합니다.")
        @ApiResponse(
                responseCode = "200",
                description = "RSS 구독 성공",
                useReturnTypeSchema = true
        )
        @PostMapping
        public ResponseTemplate<Void> subscribeRss(
//                @RequestParam Long userId,
                @RequestBody RssSubscribeRequest request
        ) {
            rssService.subscribeRss(userId, request);
            return ResponseTemplate.ok();
        }

        /**
         * RSS 구독 취소
         */
        @Operation(summary = "RSS 구독 취소", description = "사용자가 구독한 RSS를 취소합니다.")
        @ApiResponse(
                responseCode = "200",
                description = "RSS 구독 취소 성공",
                useReturnTypeSchema = true
        )
        @DeleteMapping("/{rssId}")
        public ResponseTemplate<Void> unsubscribeRss(
//                @RequestParam Long userId,
                @PathVariable Long rssId
        ) {
            rssService.unsubscribeRss(userId, rssId);
            return ResponseTemplate.ok();
        }

}
