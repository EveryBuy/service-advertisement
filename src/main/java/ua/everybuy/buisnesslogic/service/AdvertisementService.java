package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.AdvertisementRepository;

import ua.everybuy.routing.dto.StatusResponse;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final CityService cityService;
    private final AdvertisementPhotoService advertisementPhotoService;
    private final SubCategoryService subCategoryService;

    public StatusResponse createAdvertisement(CreateAdvertisementRequest request, MultipartFile[] photos) throws IOException {

        Advertisement savedAdvertisement = mapToEntity(request);
        savedAdvertisement = advertisementRepository.save(savedAdvertisement);

        List<AdvertisementPhoto> advertisementPhotos = advertisementPhotoService.handlePhotoUpload(photos,
                        savedAdvertisement.getId(),
                        subCategoryService.findById(request.subCategoryId()).toString());
        savedAdvertisement.setPhotos(advertisementPhotos);

        return StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(savedAdvertisement)
                .build();
    }
    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));
    }

    private Advertisement mapToEntity(CreateAdvertisementRequest request) {

        Advertisement advertisement = Advertisement.builder()
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .creationDate(LocalDateTime.now())
                .userId(request.userId())
                .city(cityService.findById(request.cityId()))
                .subCategory(subCategoryService.findById(request.subCategoryId()))
                .productType(request.productType())
                .deliveryMethod(request.deliveryMethod())
                .isEnabled(true)
                .creationDate(LocalDateTime.now())
                .build();
        return advertisement;
    }

}
