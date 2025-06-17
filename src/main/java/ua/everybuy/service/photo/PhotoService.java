package ua.everybuy.service.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.photo.AdvertisementPhotoRepository;
import ua.everybuy.errorhandling.custom.FileFormatException;

import javax.imageio.ImageIO;
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
    private static final int MAX_PHOTOS = 8;
    private final AdvertisementPhotoRepository advertisementPhotoRepository;
    private final AmazonS3Service amazonS3Service;

    public List<AdvertisementPhoto> uploadAndLinkPhotos(MultipartFile[] photos,
                                                        Advertisement advertisement,
                                                        String subCategoryName) throws IOException {
        validatePhotos(photos);

        List<AdvertisementPhoto> advertisementPhotos = new ArrayList<>();

        for (MultipartFile photo : photos) {
            AdvertisementPhoto advertisementPhoto = processAndUploadPhoto(photo, advertisement, subCategoryName);
            saveAdvertisementPhoto(advertisementPhoto);
            advertisementPhotos.add(advertisementPhoto);
        }

        return advertisementPhotos;
    }

    private AdvertisementPhoto processAndUploadPhoto(MultipartFile photo,
                                                     Advertisement advertisement,
                                                     String subCategoryName) throws IOException {
        isImage(photo);
        String photoUrl = amazonS3Service.uploadPhoto(photo, subCategoryName);
        return AdvertisementPhoto.builder()
                .photoUrl(photoUrl)
                .creationDate(LocalDateTime.now())
                .advertisement(advertisement)
                .build();
    }

    @Transactional
    public void deletePhotosByAdvertisementId(Advertisement advertisement) throws IOException {
        List<AdvertisementPhoto> photos = findPhotosByAdvertisement(advertisement);
        deleteAllPhotos(photos);
    }

    public void deleteAllPhotos(List<AdvertisementPhoto> photos) throws IOException {
        amazonS3Service.deletePhotos(photos);
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
}
