package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.AdvertisementRepository;

import ua.everybuy.routing.dto.AdvertisementDTO;
import ua.everybuy.routing.dto.StatusResponse;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
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

        List<AdvertisementPhoto> advertisementPhotos = uploadPhotosAndLinkToAdvertisement(photos, savedAdvertisement, request.subCategoryId());
        saveAllAdvertisementPhotos(advertisementPhotos);

        List<String> photoUrls = advertisementPhotoService.getPhotoUrlsByAdvertisementId(savedAdvertisement.getId());

        AdvertisementDTO advertisementDTO = mapToDto(savedAdvertisement, photoUrls);

        return StatusResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(advertisementDTO)
                .build();
    }

    private List<AdvertisementPhoto> uploadPhotosAndLinkToAdvertisement(MultipartFile[] photos, Advertisement advertisement, Long subCategoryId) throws IOException {
        String subCategoryName = subCategoryService.findById(subCategoryId).getSubCategoryName();
        List<AdvertisementPhoto> advertisementPhotos = advertisementPhotoService.handlePhotoUpload(photos, subCategoryName);

        advertisementPhotos.forEach(photo -> photo.setAdvertisement(advertisement));
        return advertisementPhotos;
    }

    private void saveAllAdvertisementPhotos(List<AdvertisementPhoto> advertisementPhotos) {
        advertisementPhotos.forEach(advertisementPhotoService::createAdvertisementPhoto);
    }

    public StatusResponse getAdvertisement(Long id) {
        Advertisement advertisement = findById(id);
        List<String> photoUrls = advertisementPhotoService.getPhotoUrlsByAdvertisementId(advertisement.getId());

        AdvertisementDTO advertisementDTO = mapToDto(advertisement, photoUrls);

        return StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(advertisementDTO)
                .build();
    }

    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));
    }

    private Advertisement mapToEntity(CreateAdvertisementRequest request) {

        return Advertisement.builder()
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .creationDate(LocalDateTime.now())
                .userId(request.userId())
                .city(cityService.findById(request.cityId()))
                .subCategory(subCategoryService.findById(request.subCategoryId()))
                .productType(request.productType())
                .deliveryMethods(new HashSet<>(request.deliveryMethods()))
                .isEnabled(true)
                .creationDate(LocalDateTime.now())
                .build();
    }

    public AdvertisementDTO mapToDto(Advertisement advertisement, List<String> photos) {
        return AdvertisementDTO.builder()
                .id(advertisement.getId())
                .title(advertisement.getTitle())
                .description(advertisement.getDescription())
                .price(advertisement.getPrice())
                .creationDate(advertisement.getCreationDate())
                .isEnabled(advertisement.getIsEnabled())
                .userId(advertisement.getUserId())
                .cityName(advertisement.getCity())
                .subCategoryName(advertisement.getSubCategory())
                .productType(String.valueOf(advertisement.getProductType()))
                .deliveryMethods(new HashSet<>(advertisement.getDeliveryMethods()))
                .photoUrls(photos)
                .build();
    }


}
