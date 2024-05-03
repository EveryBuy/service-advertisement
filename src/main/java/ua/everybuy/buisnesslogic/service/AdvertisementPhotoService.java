package ua.everybuy.buisnesslogic.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.AdvertisementPhoto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdvertisementPhotoService {
    @Value("${aws.access.key}")
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;
    @Value("${aws.bucket.name}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;

    public List<AdvertisementPhoto> handlePhotoUpload(MultipartFile[] photos, Long advertisementId) throws IOException {
        List<AdvertisementPhoto> advertisementPhotos = new ArrayList<>();


        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();


        if (!s3Client.doesBucketExistV2(bucketName)) {
            throw new IOException("Bucket '" + bucketName + "' does not exist.");
        }

        try {
            for (MultipartFile photo : photos) {
                String photoKey = "advertisements/" + advertisementId + "/" + photo.getOriginalFilename();

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(photo.getContentType());
                metadata.setContentLength(photo.getSize());

                s3Client.putObject(bucketName, photoKey, photo.getInputStream(), metadata);

                AdvertisementPhoto advertisementPhoto = new AdvertisementPhoto();
                advertisementPhoto.setPhotoUrl(photoKey);
                advertisementPhotos.add(advertisementPhoto);
            }
        } catch (AmazonServiceException e) {
            throw new IOException("Failed to upload photos to S3: " + e.getErrorMessage(), e);
        } catch (SdkClientException e) {
            throw new IOException("Failed to upload photos to S3: " + e.getMessage(), e);
        }

        return advertisementPhotos;
    }

}
