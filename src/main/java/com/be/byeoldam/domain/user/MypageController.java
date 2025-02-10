package com.be.byeoldam.domain.user;

import com.be.byeoldam.common.ResponseTemplate;
import com.be.byeoldam.common.annotation.UserId;
import com.be.byeoldam.domain.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Tag(name = "Mypage", description = "Mypage 관련 API")
public class MypageController {
    private final MypageService mypageService;

    // 마이페이지 조회
    @Operation(summary = "마이페이지 조회", description = "사용자의 마이페이지 정보를 조회합니다.")
    @GetMapping
    public ResponseTemplate<MypageResponse> getMyPage(@UserId long userId){
        MypageResponse response = mypageService.getMyPage(userId);
        return ResponseTemplate.ok(response);
    }


    // 마이페이지 변경
    @Operation(summary = "마이페이지 정보 변경", description = "사용자의 마이페이지 정보를 수정합니다.")
    @PatchMapping
    public ResponseTemplate<Void> updateMypage(@UserId long userId, @RequestBody MypageRequest request){
        mypageService.setMypage(userId, request);
        return ResponseTemplate.ok();
    }

    //프로필 이미지 삭제
    @Operation(summary = "프로필 이미지 삭제", description = "S3에 저장된 사용자의 프로필 이미지를 삭제합니다.")
    @DeleteMapping("profile-image")
    public ResponseTemplate<Void> deleteProfileImage(@UserId long userId){
        mypageService.deleteMypageImageToS3(userId);
        return ResponseTemplate.ok();
    }

    // 프로필 이미지 변경
    @Operation(summary = "S3에 이미지 업로드", description = "이미지를 S3에 업로드하고 URL 반환,임의로 MultilPartFile 형태로 데이터를 받았지만 필요하다면 형태 변환 가능 ")
    @PostMapping("profile-image")
    public ResponseTemplate<MypageImageResponse> uploadProfileImage(@UserId long userId,@RequestPart("upload") MultipartFile request) throws IOException {
        MypageImageResponse response = mypageService.uploadMypageImageToS3(userId, request);
        return ResponseTemplate.ok(response);
    }


    // 비밀번호 변경
    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    @PutMapping("/change-password")
    public ResponseTemplate<ChangePasswordResponse> changePassword(@UserId long userId, @RequestBody ChangePasswordRequest request){
        ChangePasswordResponse response = mypageService.changePassword(userId, request);
        return ResponseTemplate.ok(response);
    }


    // 회원 탈퇴(소프트 삭졔)
    @Operation(summary = "회원 탈퇴 (소프트 삭제)", description = "사용자의 계정을 비활성화 처리(소프트 삭제)합니다.")
    @DeleteMapping("/deactivate")
    public ResponseTemplate<Void> deactivateUser(@UserId long userId){
        mypageService.deactivateUser(userId);
        return ResponseTemplate.ok();
    }
}
