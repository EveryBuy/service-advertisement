package ua.everybuy.service.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import ua.everybuy.database.entity.AdvertisementPhoto;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonS3ServiceV2 implements S3Service {
    private final S3Client s3Client;

    @Value("${aws.photo.url}")
    private String awsUrl;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public String uploadPhoto(MultipartFile photo, String subcategory) throws IOException {
        String photoKey = generatePhotoKey(subcategory);
        String photoUrl = awsUrl + photoKey;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(photoKey)
                .contentType(photo.getContentType())
                .contentLength(photo.getSize())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(photo.getInputStream(), photo.getSize()));
        } catch (S3Exception e) {
            throw new IOException("Failed to upload photo to S3: " + e.getMessage(), e);
        }

        return photoUrl;
    }

    @Override
    public void deletePhotos(List<AdvertisementPhoto> photos) throws IOException {
        for (AdvertisementPhoto photo : photos) {
            String s3Key = photo.getPhotoUrl().replace(awsUrl, "");

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            try {
                s3Client.deleteObject(deleteObjectRequest);
            } catch (S3Exception e) {
                throw new IOException("Failed to delete photo from S3: " + e.getMessage(), e);
            }
        }
    }

    private String generatePhotoKey(String subcategory) {
        String uuid = UUID.randomUUID().toString();
        return subcategory.replaceAll("\\s+", "") + uuid;
    }
}
