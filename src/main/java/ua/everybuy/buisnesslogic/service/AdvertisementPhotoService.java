package ua.everybuy.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.AdvertisementPhotoRepository;
import ua.everybuy.errorhandling.custom.FileFormatException;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementPhotoService {
    private static final int MIN_PHOTOS = 1;
    private static final int MAX_PHOTOS = 8;
    private final AdvertisementPhotoRepository advertisementPhotoRepository;
    private final SubCategoryService subCategoryService;
    private final AmazonS3Service amazonS3Service;

    public List<AdvertisementPhoto> uploadAndLinkPhotos(MultipartFile[] photos,
                                                        Advertisement advertisement,
                                                        Long subCategoryId) throws IOException {
        validatePhotos(photos);
        String subCategoryName = subCategoryService.findById(subCategoryId).getSubCategoryName();

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

    public void deletePhotosByAdvertisementId(Long advertisementId) throws IOException {
        List<AdvertisementPhoto> photos = findPhotosByAdvertisementId(advertisementId);
        amazonS3Service.deletePhotos(photos);
        advertisementPhotoRepository.deleteAll(photos);
    }

    public List<String> getPhotoUrlsByAdvertisementId(Long advertisementId) {
        return findPhotosByAdvertisementId(advertisementId)
                .stream()
                .map(AdvertisementPhoto::getPhotoUrl)
                .collect(Collectors.toList());
    }

    public void saveAdvertisementPhoto(AdvertisementPhoto advertisementPhoto) {
        if (advertisementPhoto == null) {
            throw new IllegalArgumentException("AdvertisementPhoto object cannot be null");
        }
        advertisementPhotoRepository.save(advertisementPhoto);
    }

    public List<AdvertisementPhoto> findPhotosByAdvertisementId(Long advertisementId) {
        List<AdvertisementPhoto> photos = advertisementPhotoRepository.findByAdvertisementId(advertisementId);
        return photos;
    }

    private void isImage(MultipartFile file) throws IOException {
        if (ImageIO.read(file.getInputStream()) == null) {
            throw new FileFormatException("File should be an image");
        }
    }

    private void validatePhotos(MultipartFile[] photos) {
        if (photos == null || photos.length < MIN_PHOTOS || photos.length > MAX_PHOTOS || photos[0].isEmpty()) {
            throw new IllegalArgumentException("Number of photos must be between 1 and 8");
        }
    }
}
