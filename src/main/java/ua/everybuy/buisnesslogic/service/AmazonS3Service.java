package ua.everybuy.buisnesslogic.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.AdvertisementPhoto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    private final AmazonS3 s3Client;

    @Value("${aws.photo.url}")
    private String awsUrl;
    @Value("${aws.bucket.name}")
    private String bucketName;

    public String uploadPhoto(MultipartFile photo, String subcategory) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String photoKey = subcategory.replaceAll("\\s+", "") + uuid;
        String photoUrl = awsUrl + photoKey;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(photo.getContentType());
        metadata.setContentLength(photo.getSize());

        try (InputStream inputStream = photo.getInputStream()) {
            s3Client.putObject(bucketName, photoKey, inputStream, metadata);
        } catch (AmazonServiceException e) {
            throw new IOException("Failed to upload photo to S3: " + e.getErrorMessage(), e);
        } catch (SdkClientException e) {
            throw new IOException("Failed to upload photo to S3: " + e.getMessage(), e);
        }

        return photoUrl;
    }

    public void deletePhotos(List<AdvertisementPhoto> photos) throws IOException {
        for (AdvertisementPhoto photo : photos) {
            String photoUrl = photo.getPhotoUrl();
            String s3Key = photoUrl.replace(awsUrl, "");

            try {
                s3Client.deleteObject(bucketName, s3Key);
            } catch (AmazonServiceException e) {
                throw new IOException("Failed to upload photo to S3: " + e.getErrorMessage(), e);
            } catch (SdkClientException e) {
                throw new IOException("Failed to upload photo to S3: " + e.getMessage(), e);
            }
        }
    }
}
