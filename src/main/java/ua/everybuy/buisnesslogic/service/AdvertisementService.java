package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.integration.UserProfileService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.database.repository.AdvertisementRepository;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.mapper.AdvertisementMapper;
import ua.everybuy.routing.dto.request.UpdateAdvertisementRequest;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;
import ua.everybuy.routing.dto.response.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementSubCategoryService advertisementSubCategoryService;
    private final PhotoService photoService;
    private final StatisticsService statisticsService;
    private final AdvertisementMapper advertisementMapper;
    private final UserProfileService userProfileService;
    private final DeliveryService deliveryService;

    public StatusResponse<CreateAdvertisementResponse> createAdvertisement(CreateAdvertisementRequest createRequest,
                                                                           MultipartFile[] photos,
                                                                           String userId) throws IOException {

        Advertisement newAdvertisement = createAndSaveAdvertisement(createRequest, userId);

        // Save the associated photos and update the main photo URL
        saveAdvertisementPhotos(photos, newAdvertisement, createRequest.topSubCategoryId());
        List<String> photoUrls = photoService.getPhotoUrlsByAdvertisementId(newAdvertisement.getId());

        // Save delivery methods and retrieve them
        deliveryService.saveAdvertisementDeliveries(newAdvertisement, createRequest.deliveryMethods());
        Set<String> deliveryMethods = deliveryService.getAdvertisementDeliveryMethods(newAdvertisement);

        // Map the saved advertisement to the response DTO and return it
        CreateAdvertisementResponse advertisementResponse = advertisementMapper
                .mapToAdvertisementCreateResponse(newAdvertisement, deliveryMethods, photoUrls);

        return new StatusResponse<>(HttpStatus.CREATED.value(), advertisementResponse);
    }

    private Advertisement createAndSaveAdvertisement(CreateAdvertisementRequest createRequest, String userId) {
        TopLevelSubCategory topLevelSubCategory = advertisementSubCategoryService.getTopLevelSubCategory(createRequest);
        LowLevelSubCategory lowLevelSubCategory = advertisementSubCategoryService.getLowLevelSubCategory(createRequest);
        Advertisement advertisement = advertisementMapper
                .mapToEntity(createRequest, Long.parseLong(userId), topLevelSubCategory, lowLevelSubCategory);
        return advertisementRepository.save(advertisement);
    }

    public StatusResponse<UpdateAdvertisementResponse> updateAdvertisement(Long advertisementId,
                                                                           UpdateAdvertisementRequest updateRequest,
                                                                           MultipartFile[] newPhotos,
                                                                           String userId) throws IOException {

        Advertisement existingAdvertisement = findAdvertisementByIdAndUserId(advertisementId, Long.parseLong(userId));

        TopLevelSubCategory topLevelSubCategory = advertisementSubCategoryService.getTopLevelSubCategory(updateRequest);
        LowLevelSubCategory lowLevelSubCategory = advertisementSubCategoryService.getLowLevelSubCategory(updateRequest);

        photoService.deletePhotosByAdvertisementId(existingAdvertisement.getId());
        existingAdvertisement = advertisementMapper.mapToEntity(updateRequest, existingAdvertisement,
                topLevelSubCategory, lowLevelSubCategory);

        saveAdvertisementPhotos(newPhotos, existingAdvertisement, updateRequest.topSubCategoryId());
        List<String> updatedPhotos = photoService.getPhotoUrlsByAdvertisementId(existingAdvertisement.getId());

        deliveryService.updateAdvertisementDeliveries(existingAdvertisement, updateRequest.deliveryMethods());
        Set<String> deliveryMethods = deliveryService.getAdvertisementDeliveryMethods(existingAdvertisement);

        UpdateAdvertisementResponse updateAdvertisementResponse = advertisementMapper
                .mapToAdvertisementUpdateResponse(existingAdvertisement, deliveryMethods, updatedPhotos);
        return new StatusResponse<>(HttpStatus.OK.value(), updateAdvertisementResponse);
    }


    private void saveAdvertisementPhotos(MultipartFile[] newPhotos,
                                         Advertisement existingAdvertisement,
                                         Long advertisementId) throws IOException {
        List<AdvertisementPhoto> existingPhotos = photoService
                .uploadAndLinkPhotos(newPhotos, existingAdvertisement, advertisementId);

        String mainPhotoUrl = existingPhotos.get(0).getPhotoUrl();
        existingAdvertisement.setMainPhotoUrl(mainPhotoUrl);
        advertisementRepository.save(existingAdvertisement);
    }

    public Advertisement findActiveAdvertisementById(Long id) {
        Advertisement advertisement = findById(id);
        if (!advertisement.getIsEnabled()) {
            throw new AccessDeniedException("Advertisement is inactive");
        }
        return advertisement;
    }

    public StatusResponse<AdvertisementDto> getAdvertisement(Long id) {
        Advertisement advertisement = findActiveAdvertisementById(id);
        statisticsService.incrementViewsAndSave(advertisement);
        AdvertisementDto advertisementDTO = createAdvertisementDto(advertisement, advertisement.getUserId());

        return new StatusResponse<>(HttpStatus.OK.value(), advertisementDTO);
    }

    public AdvertisementDto createAdvertisementDto(Advertisement advertisement,
                                                   Long userId) {

        List<String> photoUrls = photoService.getPhotoUrlsByAdvertisementId(advertisement.getId());
        Set<String> deliveryMethods = deliveryService.getAdvertisementDeliveryMethods(advertisement);

        AdvertisementDto advertisementDTO = advertisementMapper.mapToDto(advertisement, deliveryMethods, photoUrls);
        advertisementDTO.setUserDto(userProfileService.getShortUserInfo(userId));

        return advertisementDTO;
    }

    public void deleteAdvertisement(Long advertisementId, Principal principal) throws IOException {
        Advertisement advertisement = findAdvertisementByIdAndUserId(advertisementId, Long.parseLong(principal.getName()));
        photoService.deletePhotosByAdvertisementId(advertisement.getId());
        advertisementRepository.delete(advertisement);

    }

    public StatusResponse<AdvertisementStatusResponse> setAdvertisementEnabledStatus(Long id) {
        Advertisement advertisement = findById(id);

        boolean currentStatus = advertisement.getIsEnabled();
        advertisement.setIsEnabled(!currentStatus);
        advertisement.setUpdateDate(LocalDateTime.now());
        advertisementRepository.save(advertisement);

        AdvertisementStatusResponse advertisementStatusResponse =
                advertisementMapper.mapToAdvertisementStatusResponse(advertisement);

        return new StatusResponse<>(HttpStatus.OK.value(), advertisementStatusResponse);
    }

    public Advertisement findById(Long id) {
        return advertisementRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));
    }

    public List<Advertisement> findAllUserAdvertisement(Long userId) {
        List<Advertisement> userAdvertisement = advertisementRepository.findByUserId(userId);
        if (userAdvertisement == null || userAdvertisement.isEmpty()) {
            throw new EntityNotFoundException("No advertisements found for the given user " + userId);
        }
        return userAdvertisement;
    }

    public Advertisement findAdvertisementByIdAndUserId(Long advertisementId, Long userId) {
        return advertisementRepository.findByIdAndUserId(advertisementId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id "
                                + userId
                                + " does not have an advertisement with the id "
                                + advertisementId
                                + " or advertisement not found."));

    }

    public List<Advertisement> findAllEnabledAdsOrderByCreationDateDesc() {
        return advertisementRepository.findByIsEnabledTrueOrderByCreationDateDesc();
    }

    public AdvertisementInfoForChatService getAdvertisementShortInfo(Long advertisementId) {
        Advertisement advertisement = findActiveAdvertisementById(advertisementId);
        return advertisementMapper.mapToAdvertisementInfoForChatService(advertisement);
    }
}
