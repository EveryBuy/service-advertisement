package ua.everybuy.service.photo;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.photo.AdvertisementPhotoRepository;
import ua.everybuy.errorhandling.custom.FileFormatException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ua.everybuy.errorhandling.message.PhotoValidationMessages.*;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private static final int MIN_PHOTOS = 1;
    private static final int MAX_PHOTOS = 10;
    private final AdvertisementPhotoRepository advertisementPhotoRepository;
    private final S3Service s3Service;

    public List<AdvertisementPhoto> uploadAndLinkPhotos(MultipartFile[] photos,
                                                        Advertisement advertisement,
                                                        List<Byte> rotations,
                                                        String subCategoryName) throws IOException {
        validatePhotos(photos);
        validateRotations(photos, rotations);

        List<AdvertisementPhoto> advertisementPhotos = new ArrayList<>();

        for (int i = 0; i < photos.length; i++) {
            AdvertisementPhoto advertisementPhoto =
                    processAndUploadPhoto(photos[i], advertisement, rotations.get(i), subCategoryName);
            saveAdvertisementPhoto(advertisementPhoto);
            advertisementPhotos.add(advertisementPhoto);
        }

        return advertisementPhotos;
    }

    private AdvertisementPhoto processAndUploadPhoto(MultipartFile photo, Advertisement advertisement, Byte rotation, String subCategoryName) throws IOException {
        isImage(photo);
        String photoUrl;
        if (rotation == 0) {
            photoUrl = s3Service.uploadPhoto(photo, subCategoryName);
        } else {
            String fileFormat = getFormatName(photo);
            BufferedImage originalImage = ImageIO.read(photo.getInputStream());
            BufferedImage rotatedImage = rotateImage(originalImage, rotation);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(rotatedImage, fileFormat, os);
            byte[] rotatedBytes = os.toByteArray();
            photoUrl = s3Service.uploadPhoto(rotatedBytes, fileFormat, subCategoryName);
        }
        return AdvertisementPhoto.builder().photoUrl(photoUrl).creationDate(LocalDateTime.now()).advertisement(advertisement).build();
    }

    @Transactional
    public void deletePhotosByAdvertisementId(Advertisement advertisement) throws IOException {
        List<AdvertisementPhoto> photos = findPhotosByAdvertisement(advertisement);
        deleteAllPhotos(photos);
    }

    public void deleteAllPhotos(List<AdvertisementPhoto> photos) throws IOException {
        s3Service.deletePhotos(photos);
        advertisementPhotoRepository.deleteAll(photos);
    }

    public List<String> getPhotoUrlsByAdvertisementId(Advertisement advertisement) {
        return findPhotosByAdvertisement(advertisement)
                .stream()
                .map(AdvertisementPhoto::getPhotoUrl)
                .collect(Collectors.toList());
    }

    public void saveAdvertisementPhoto(AdvertisementPhoto advertisementPhoto) {
        if (advertisementPhoto == null) {
            throw new IllegalArgumentException(NULL_ADVERTISEMENT_PHOTO_ERROR);
        }
        advertisementPhotoRepository.save(advertisementPhoto);
    }

    public List<AdvertisementPhoto> findPhotosByAdvertisement(Advertisement advertisement) {
        return advertisementPhotoRepository
                .findByAdvertisement(advertisement);
    }

    private void isImage(MultipartFile file) throws IOException {
        if (ImageIO.read(file.getInputStream()) == null) {
            throw new FileFormatException(PHOTO_UPLOAD_ERROR);
        }
    }

    private void validatePhotos(MultipartFile[] photos) {
        if (photos == null || photos.length < MIN_PHOTOS
                || photos.length > MAX_PHOTOS || photos[0].isEmpty()) {
            throw new IllegalArgumentException(String.format(
                    INVALID_PHOTO_COUNT_ERROR, MIN_PHOTOS, MAX_PHOTOS));
        }
    }

    private void validateRotations(MultipartFile[] photos, List<Byte> rotations) {
        if (rotations == null || rotations.size() != photos.length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid rotations count: expected %d but got %d",
                    photos.length,
                    rotations == null ? 0 : rotations.size()
            ));
        }
    }

    private BufferedImage rotateImage(BufferedImage originalImage, int rotation) {
        if (rotation < 0 || rotation > 3) return originalImage;

        double radians = Math.toRadians(rotation * 90);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int newWidth = (rotation % 2 == 0) ? width : height;
        int newHeight = (rotation % 2 == 0) ? height : width;

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2d = rotated.createGraphics();
        g2d.rotate(radians, newWidth / 2.0, newHeight / 2.0);
        g2d.drawImage(originalImage, (newWidth - width) / 2, (newHeight - height) / 2, null);
        g2d.dispose();

        return rotated;
    }

    private String getFormatName(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
            switch (ext) {
                case "png":
                case "jpg":
                case "jpeg":
                case "bmp":
                case "gif":
                case "webp":
                    return ext.equals("jpeg") ? "jpg" : ext;
            }
        }
        throw new FileFormatException("Unsupported image format");
    }

}
