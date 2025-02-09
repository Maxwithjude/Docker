package com.be.byeoldam.domain.user;


import com.be.byeoldam.domain.user.dto.MypageImageResponse;
import com.be.byeoldam.domain.user.dto.S3UploadResponse;
import com.be.byeoldam.domain.user.model.S3Image;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.S3ImageRepository;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.domain.user.util.S3Util;
import com.be.byeoldam.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final S3Util s3Util;
    private final UserRepository userRepository;
    private final S3ImageRepository s3ImageRepository;

    @Transactional
    MypageImageResponse uploadMypageImageToS3(long userId, MultipartFile multipartFile) throws IOException {
        User user =userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("해당 userId와 매핑된 User가 없습니다."));

        // 기존에 이미지가 있다면 삭제하기.
        s3ImageRepository.findByFileUrl(user.getProfileUrl())
                .ifPresent(s3Image -> {
                    String s3Key = s3Image.getFileKey();
                    s3ImageRepository.delete(s3Image);
                    s3Util.deleteImage(s3Key);
                });

        // 새로운 이미지 추가하기 S3에 업로드
        S3UploadResponse s3UploadResponse = s3Util.imageUpload(multipartFile,"mypage");

        // 새로운 이미지 DB에 추가하기
        user.updateProfileImage(s3UploadResponse.getS3Url());
        S3Image s3Image = s3UploadResponse.toEntity();
        s3ImageRepository.save(s3Image);

        return MypageImageResponse.of(s3UploadResponse.getS3Url());
    }

}
