package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.AdvertisementRepository;
import ua.everybuy.database.repository.CityRepository;
import ua.everybuy.database.repository.SubCategoryRepository;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final CityRepository cityRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final AdvertisementPhotoService advertisementPhotoService;

    @Transactional
    public Advertisement createAdvertisement(CreateAdvertisementRequest request, Long userId, MultipartFile[] photos) throws IOException {
        Advertisement advertisement = mapToEntity(request, userId);

        Advertisement savedAdvertisement = advertisementRepository.save(advertisement);

        List<AdvertisementPhoto> advertisementPhotos = advertisementPhotoService.handlePhotoUpload(photos, savedAdvertisement.getId());
        savedAdvertisement.setPhotos(advertisementPhotos);


        return advertisementRepository.save(savedAdvertisement);
    }

    private Advertisement mapToEntity(CreateAdvertisementRequest request, Long userId) {

        Advertisement advertisement = Advertisement.builder()
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .creationDate(LocalDateTime.now())
                .userId(userId)
                .city(cityRepository.findById(request.cityId()).orElseThrow(() -> new EntityNotFoundException("City not found")))
                .subCategory(subCategoryRepository.findById(request.subCategoryId()).orElseThrow(() -> new EntityNotFoundException("Subcategory not found")))
                .productType(request.productType())
                .deliveryMethod(request.deliveryMethod())
                .build();
        return advertisement;
    }

}
