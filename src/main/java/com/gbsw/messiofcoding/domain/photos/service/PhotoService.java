package com.gbsw.messiofcoding.domain.photos.service;

import com.gbsw.messiofcoding.domain.photos.dto.response.PhotoUploadResponse;
import com.gbsw.messiofcoding.domain.photos.entity.Photo;
import com.gbsw.messiofcoding.domain.photos.repository.PhotoRepository;
import com.gbsw.messiofcoding.global.exception.CustomException;
import com.gbsw.messiofcoding.global.exception.ErrorCode;
import com.gbsw.messiofcoding.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final S3Service s3Service;
    private final PhotoRepository photoRepository;

    public PhotoUploadResponse upload(MultipartFile file, Long userId) {
        S3Service.S3UploadResult result = s3Service.upload(file, userId);

        Photo photo = Photo.builder()
                .userId(userId)
                .s3Key(result.s3Key())
                .imageUrl(result.imageUrl())
                .build();

        Photo saved = photoRepository.save(photo);

        return PhotoUploadResponse.from(saved);
    }

    public void delete(UUID photoId, Long userId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND));

        if (!photo.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        s3Service.delete(photo.getS3Key());
        photoRepository.delete(photo);
    }
}
