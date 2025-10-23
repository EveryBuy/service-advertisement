package ua.everybuy.service.photo;

import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.AdvertisementPhoto;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    /**
     * Uploads a photo to S3 and returns its URL.
     *
     * @param photo       file to upload
     * @param subcategory subcategory used to generate the key
     * @return URL of the uploaded photo
     * @throws IOException if the upload fails
     */
    String uploadPhoto(MultipartFile photo, String subcategory) throws IOException;

    /**
     * Uploads a photo from byte array data to S3 storage and returns its public URL.
     * This method is useful for processed images (e.g., rotated, resized) that exist
     * as byte arrays in memory rather than as files.
     *
     * @param formatType the image format extension (e.g., "jpg", "png", "webp"),
     *                   must not be blank and must be supported
     */
    String uploadPhoto(byte[] photo, String formatType, String subcategory) throws IOException;

    /**
     * Deletes a list of photos from S3.
     *
     * @param photos list of AdvertisementPhoto objects to delete
     * @throws IOException if deletion fails
     */
    void deletePhotos(List<AdvertisementPhoto> photos) throws IOException;
}
