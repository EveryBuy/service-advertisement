package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementPhotoService advertisementPhotoService;
    private final AdvertisementMapper advertisementMapper;
    private final UserService userService;

    public StatusResponse createAdvertisement(CreateAdvertisementRequest createRequest,
                                              MultipartFile[] photos,
                                              String userId) throws IOException {

        Advertisement savedAdvertisement = advertisementMapper.mapToEntity(createRequest, Long.parseLong(userId));
        savedAdvertisement = advertisementRepository.save(savedAdvertisement);

        savedAdvertisement(photos, savedAdvertisement, createRequest.subCategoryId());

        List<String> photoUrls = advertisementPhotoService.getPhotoUrlsByAdvertisementId(savedAdvertisement.getId());

        CreateAdvertisementResponse advertisementResponse = advertisementMapper.
                mapToAdvertisementCreateResponse(savedAdvertisement, photoUrls);

        return StatusResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(advertisementResponse)
                .build();
    }

    public StatusResponse updateAdvertisement(Long advertisementId,
                                              UpdateAdvertisementRequest updateRequest,
                                              MultipartFile[] newPhotos, String userId) throws IOException {

        Advertisement existingAdvertisement = findAdvertisementByIdAndUserId(advertisementId, Long.parseLong(userId));

        existingAdvertisement = advertisementMapper.mapToEntity(updateRequest, existingAdvertisement);
        advertisementPhotoService.deletePhotosByAdvertisementId(existingAdvertisement.getId());

        savedAdvertisement(newPhotos, existingAdvertisement, updateRequest.subCategoryId());
        List<String> updatedPhotos = advertisementPhotoService.getPhotoUrlsByAdvertisementId(existingAdvertisement.getId());

        UpdateAdvertisementResponse updateAdvertisementResponse = advertisementMapper
                .mapToAdvertisementUpdateResponse(existingAdvertisement, updatedPhotos);

        return StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(updateAdvertisementResponse)
                .build();
    }

    private void savedAdvertisement(MultipartFile[] newPhotos,
                                    Advertisement existingAdvertisement,
                                    Long advertisementId) throws IOException {
        List<AdvertisementPhoto> existingPhotos = advertisementPhotoService
                .uploadPhotosAndLinkToAdvertisement(newPhotos, existingAdvertisement, advertisementId);

        String mainPhotoUrl = existingPhotos.get(0).getPhotoUrl();
        existingAdvertisement.setMainPhotoUrl(mainPhotoUrl);
        advertisementRepository.save(existingAdvertisement);
    }

    public StatusResponse getActiveAdvertisement(Long id, HttpServletRequest request) {
        Advertisement advertisement = findById(id);
        if (!advertisement.getIsEnabled()) {
            throw new AccessDeniedException("Advertisement is inactive");
        }

        incrementViewsAndSave(id);
        AdvertisementDto advertisementDTO = createAdvertisementDto(advertisement, advertisement.getUserId(), request);

        return StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(advertisementDTO)
                .build();
    }

    public StatusResponse getUserAdvertisement(Long id, HttpServletRequest request, Principal principal) {

        Long userId = Long.parseLong(principal.getName());
        Advertisement advertisement = findAdvertisementByIdAndUserId(id, userId);
        AdvertisementDto advertisementDTO = createAdvertisementDto(advertisement, userId, request);

        return StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(advertisementDTO)
                .build();
    }

    private void incrementViewsAndSave(Long advertisementId) {
        Advertisement advertisement = findById(advertisementId);
        if (advertisement != null) {
            advertisement.setViews(advertisement.getViews() + 1);
            advertisementRepository.save(advertisement);
        }
    }
    public void incrementFavouriteCountAndSave(Advertisement advertisement) {
        if (advertisement != null) {
            advertisement.setFavouriteCount(advertisement.getFavouriteCount()+1);
            advertisementRepository.save(advertisement);
        }
    }

    private AdvertisementDto createAdvertisementDto(Advertisement advertisement,
                                                    Long userId,
                                                    HttpServletRequest request) {

        List<String> photoUrls = advertisementPhotoService.getPhotoUrlsByAdvertisementId(advertisement.getId());
        AdvertisementDto advertisementDTO = advertisementMapper.mapToDto(advertisement, photoUrls);
        advertisementDTO.setUserDto(userService.getShortUserInfo(request, userId));

        return advertisementDTO;
    }


    public void deleteAdvertisement(Long advertisementId, Principal principal) throws IOException {
        Advertisement advertisement = findAdvertisementByIdAndUserId(advertisementId, Long.parseLong(principal.getName()));

        advertisementPhotoService.deletePhotosByAdvertisementId(advertisement.getId());
        advertisementRepository.delete(advertisement);

    }

    public StatusResponse setAdvertisementEnabledStatus(Long id) {
        Advertisement advertisement = findById(id);
        boolean currentStatus = advertisement.getIsEnabled();
        advertisement.setIsEnabled(!currentStatus);
        advertisement.setUpdateDate(LocalDateTime.now());
        advertisementRepository.save(advertisement);
        AdvertisementStatusResponse advertisementStatusResponse =
                advertisementMapper.mapToAdvertisementStatusResponse(advertisement);

        return StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(advertisementStatusResponse)
                .build();
    }

    public List<ShortAdvertisementResponse> getUserAdvertisementsByEnabledStatus(Long userId, boolean isEnabled) {
        return findAllUserAdvertisement(userId)
                .stream()
                .filter(ad -> ad.getIsEnabled() == isEnabled)
                .map(advertisementMapper::mapToShortAdvertisementResponse)
                .toList();
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

    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }
    public List<Advertisement> findAllEnableAds() {
        return findAll()
                .stream()
                .filter(Advertisement::getIsEnabled)
                .collect(Collectors.toList());
    }
}
