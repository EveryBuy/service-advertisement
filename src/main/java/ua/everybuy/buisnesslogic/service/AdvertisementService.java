package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.integration.UserProfileService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
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

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementPhotoService advertisementPhotoService;
    private final AdvertisementMapper advertisementMapper;
    private final UserProfileService userProfileService;

    public StatusResponse<CreateAdvertisementResponse> createAdvertisement(CreateAdvertisementRequest createRequest,
                                                                           MultipartFile[] photos,
                                                                           String userId) throws IOException {

        Advertisement newAdvertisement = advertisementMapper.mapToEntity(createRequest, Long.parseLong(userId));
        newAdvertisement = advertisementRepository.save(newAdvertisement);

        savedAdvertisementPhotos(photos, newAdvertisement, createRequest.subCategoryId());
        List<String> photoUrls = advertisementPhotoService.getPhotoUrlsByAdvertisementId(newAdvertisement.getId());

        CreateAdvertisementResponse advertisementResponse = advertisementMapper.
                mapToAdvertisementCreateResponse(newAdvertisement, photoUrls);

        return new StatusResponse<>(HttpStatus.CREATED.value(), advertisementResponse);
    }

    public StatusResponse<UpdateAdvertisementResponse> updateAdvertisement(Long advertisementId,
                                                                           UpdateAdvertisementRequest updateRequest,
                                                                           MultipartFile[] newPhotos, String userId) throws IOException {

        Advertisement existingAdvertisement = findAdvertisementByIdAndUserId(advertisementId, Long.parseLong(userId));

        existingAdvertisement = advertisementMapper.mapToEntity(updateRequest, existingAdvertisement);
        advertisementPhotoService.deletePhotosByAdvertisementId(existingAdvertisement.getId());

        savedAdvertisementPhotos(newPhotos, existingAdvertisement, updateRequest.subCategoryId());
        List<String> updatedPhotos = advertisementPhotoService.getPhotoUrlsByAdvertisementId(existingAdvertisement.getId());

        UpdateAdvertisementResponse updateAdvertisementResponse = advertisementMapper
                .mapToAdvertisementUpdateResponse(existingAdvertisement, updatedPhotos);

        return new StatusResponse<>(HttpStatus.OK.value(), updateAdvertisementResponse);
    }

    private void savedAdvertisementPhotos(MultipartFile[] newPhotos,
                                          Advertisement existingAdvertisement,
                                          Long advertisementId) throws IOException {
        List<AdvertisementPhoto> existingPhotos = advertisementPhotoService
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

    public StatusResponse<AdvertisementDto> getAdvertisement(Long id, HttpServletRequest request) {
        Advertisement advertisement = findActiveAdvertisementById(id);
        incrementViewsAndSave(advertisement);
        AdvertisementDto advertisementDTO = createAdvertisementDto(advertisement, advertisement.getUserId(), request);

        return new StatusResponse<>(HttpStatus.OK.value(), advertisementDTO);
    }

    private void incrementViewsAndSave(Advertisement advertisement) {
        if (advertisement != null) {
            advertisement.setViews(advertisement.getViews() + 1);
            advertisementRepository.save(advertisement);
        }
    }

    public void incrementFavouriteCountAndSave(Advertisement advertisement) {
        if (advertisement != null) {
            advertisement.setFavouriteCount(advertisement.getFavouriteCount() + 1);
            advertisementRepository.save(advertisement);
        }
    }

    public AdvertisementDto createAdvertisementDto(Advertisement advertisement,
                                            Long userId,
                                            HttpServletRequest request) {

        List<String> photoUrls = advertisementPhotoService.getPhotoUrlsByAdvertisementId(advertisement.getId());
        AdvertisementDto advertisementDTO = advertisementMapper.mapToDto(advertisement, photoUrls);
        advertisementDTO.setUserDto(userProfileService.getShortUserInfo(userId));

        return advertisementDTO;
    }

    public void deleteAdvertisement(Long advertisementId, Principal principal) throws IOException {
        Advertisement advertisement = findAdvertisementByIdAndUserId(advertisementId, Long.parseLong(principal.getName()));
        advertisementPhotoService.deletePhotosByAdvertisementId(advertisement.getId());
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
