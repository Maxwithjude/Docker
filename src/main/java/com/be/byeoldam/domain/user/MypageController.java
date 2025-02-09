package com.be.byeoldam.domain.user;

import com.be.byeoldam.common.ResponseTemplate;
import com.be.byeoldam.common.annotation.UserId;
import com.be.byeoldam.domain.user.dto.MypageImageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Tag(name = "Mypage", description = "Mypage 관련 API")
public class MypageController {
    private final MypageService mypageService;

    @Operation(summary = "S3에 이미지 업로드", description = "이미지를 S3에 업로드하고 URL 반환,임의로 MultilPartFile 형태로 데이터를 받았지만 필요하다면 형태 변환 가능 ")
    @PostMapping("profile-image")
    public ResponseTemplate<MypageImageResponse> uploadProfileImage(@UserId long userId,@RequestPart("upload") MultipartFile request) throws IOException {
        MypageImageResponse response = mypageService.uploadMypageImageToS3(userId, request);
        return ResponseTemplate.ok(response);
    }
}
