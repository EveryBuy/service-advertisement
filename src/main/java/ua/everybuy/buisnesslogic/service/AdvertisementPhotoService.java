package ua.everybuy.buisnesslogic.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.AdvertisementPhotoRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementPhotoService {

    private final AdvertisementPhotoRepository advertisementPhotoRepository;
    private final AmazonS3 s3Client;

    @Value("${aws.photo.url}")
    private String awsUrl;
    @Value("${aws.bucket.name}")
    private String bucketName;

    public List<AdvertisementPhoto> handlePhotoUpload(MultipartFile[] photos, String subcategory) throws IOException {
        List<AdvertisementPhoto> advertisementPhotos = new ArrayList<>();

        for (MultipartFile photo : photos) {
            String photoUrl = uploadPhotoToS3(photo, subcategory);
            advertisementPhotos.add(AdvertisementPhoto.builder()
                    .photoUrl(photoUrl)
                    .creationDate(LocalDateTime.now())
                    .build());
        }

        return advertisementPhotos;
    }

    public void deletePhotosByAdvertisementId(Long advertisementId) throws IOException {
        List<AdvertisementPhoto> photos = advertisementPhotoRepository.findByAdvertisementId(advertisementId);

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

        advertisementPhotoRepository.deleteAll(photos);
    }

    public List<String> getPhotoUrlsByAdvertisementId(Long advertisementId) {
        return advertisementPhotoRepository.findByAdvertisementId(advertisementId)
                .stream()
                .map(AdvertisementPhoto::getPhotoUrl)
                .collect(Collectors.toList());
    }

    public void createAdvertisementPhoto(AdvertisementPhoto advertisementPhoto) {
        if (advertisementPhoto == null) {
            throw new IllegalArgumentException("AdvertisementPhoto object cannot be null");
        }
        advertisementPhotoRepository.save(advertisementPhoto);
    }

    private String uploadPhotoToS3(MultipartFile photo, String subcategory) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String photoKey = subcategory.replaceAll("\\s+", "") + "/" + uuid;
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
}
