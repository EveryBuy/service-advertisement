package ua.everybuy.buisnesslogic.service.advertisement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.photo.PhotoService;
import ua.everybuy.buisnesslogic.service.integration.UserProfileService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.AdvertisementRepository;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.ShortUserInfoDto;
import ua.everybuy.routing.dto.mapper.AdvertisementMapper;
import ua.everybuy.routing.dto.response.*;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdvertisementManagementService {
    private final AdvertisementRepository advertisementRepository;
    private final PhotoService photoService;
    private final StatisticsService statisticsService;
    private final AdvertisementMapper advertisementMapper;
    private final UserProfileService userProfileService;
    private final DeliveryService deliveryService;

    public Advertisement saveAdvertisement(Advertisement advertisement) {
        validateAdvertisement(advertisement);
        return advertisementRepository.save(advertisement);
    }

    public void uploadAndSaveAdvertisementPhotos(MultipartFile[] newPhotos,
                                                 Advertisement existingAdvertisement,
                                                 Long advertisementId) throws IOException {
        List<AdvertisementPhoto> photos = photoService.uploadAndLinkPhotos(newPhotos,
                existingAdvertisement, advertisementId);
        updateMainPhoto(existingAdvertisement, photos);
    }

    private void updateMainPhoto(Advertisement existingAdvertisement, List<AdvertisementPhoto> photos) {
        if (photos != null && !photos.isEmpty()) {
            String mainPhotoUrl = photos.get(0).getPhotoUrl();
            existingAdvertisement.setMainPhotoUrl(mainPhotoUrl);
            advertisementRepository.save(existingAdvertisement);
        }
    }

    public Advertisement findActiveAdvertisementById(Long id) {
        Advertisement advertisement = findById(id);
        validateAdvertisementIsActive(advertisement);
        return advertisement;
    }

    public StatusResponse<AdvertisementDto> getActiveAdvertisement(Long id) {
        Advertisement advertisement = findActiveAdvertisementById(id);
        statisticsService.incrementViewsAndSave(advertisement);
        return new StatusResponse<>(HttpStatus.OK.value(), createAdvertisementDto(advertisement));
    }

    public StatusResponse<AdvertisementDto> retrieveAdvertisementWithAuthorization(Long id, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        Advertisement advertisement =findById(id);

        if (advertisement.getIsEnabled()) {
            AdvertisementDto advertisementDTO = createAdvertisementDto(advertisement);
            return new StatusResponse<>(HttpStatus.OK.value(), advertisementDTO);
        }

        validateUserAccessToAdvertisement(advertisement, userId);
        AdvertisementDto advertisementDTO = createAdvertisementDto(advertisement);
        return new StatusResponse<>(HttpStatus.OK.value(), advertisementDTO);
    }

    AdvertisementDto createAdvertisementDto(Advertisement advertisement) {
        List<String> photoUrls = photoService.getPhotoUrlsByAdvertisementId(advertisement.getId());
        Set<String> deliveryMethods = deliveryService.getAdvertisementDeliveryMethods(advertisement);
        ShortUserInfoDto userDto = userProfileService.getShortUserInfo(advertisement.getUserId());

        return advertisementMapper.mapToDto(advertisement, deliveryMethods, photoUrls, userDto);
    }

    public void deleteAdvertisement(Long advertisementId, Principal principal) throws IOException {
        Advertisement advertisement = findAdvertisementByIdAndUserId(advertisementId, Long.parseLong(principal.getName()));
        photoService.deletePhotosByAdvertisementId(advertisement.getId());
        advertisementRepository.delete(advertisement);
    }

    public StatusResponse<AdvertisementStatusResponse> setAdvertisementEnabledStatus(Long id) {
        Advertisement advertisement = findById(id);
        toggleAdvertisementStatus(advertisement);
        return new StatusResponse<>(HttpStatus.OK.value(),
                advertisementMapper.mapToAdvertisementStatusResponse(advertisement));
    }

    private void toggleAdvertisementStatus(Advertisement advertisement) {
        boolean currentStatus = advertisement.getIsEnabled();
        advertisement.setIsEnabled(!currentStatus);
        advertisement.setUpdateDate(LocalDateTime.now());
        advertisementRepository.save(advertisement);
    }

    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id)
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
                        String.format("User with id %d "
                                + "does not have an advertisement with id %d", userId, advertisementId)));
    }

    public List<Advertisement> findAllEnabledAdsOrderByCreationDateDesc() {
        return advertisementRepository.findByIsEnabledTrueOrderByCreationDateDesc();
    }

    public AdvertisementInfoForChatService getAdvertisementShortInfo(Long advertisementId) {
        Advertisement advertisement = findActiveAdvertisementById(advertisementId);
        return advertisementMapper.mapToAdvertisementInfoForChatService(advertisement);
    }

    private void validateAdvertisement(Advertisement advertisement) {
        if (advertisement == null) {
            throw new IllegalArgumentException("Advertisement cannot be null");
        }
    }

    private void validateAdvertisementIsActive(Advertisement advertisement) {
        if (!advertisement.getIsEnabled()) {
            throw new AccessDeniedException("Advertisement is inactive");
        }
    }
    private void validateUserAccessToAdvertisement(Advertisement advertisement, Long userId) {
        if (!advertisement.getUserId().equals(userId)) {
            throw new AccessDeniedException("User with ID "
                    + userId
                    + " does not have permission to view advertisement with ID "
                    + advertisement.getId() + ".");
        }
    }
}
