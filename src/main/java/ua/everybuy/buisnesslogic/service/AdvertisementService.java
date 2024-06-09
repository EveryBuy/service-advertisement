package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.AdvertisementRepository;

import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.mapper.AdvertisementMapper;
import ua.everybuy.routing.dto.response.AdvertisementStatusResponse;
import ua.everybuy.routing.dto.response.StatusResponse;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementPhotoService advertisementPhotoService;
    private final AdvertisementMapper advertisementMapper;
    private final SubCategoryService subCategoryService;
    private final UserService userService;

    public StatusResponse createAdvertisement(CreateAdvertisementRequest request, MultipartFile[] photos, String userId) throws IOException {

        Advertisement savedAdvertisement = advertisementMapper.mapToEntity(request);
        savedAdvertisement.setUserId(Long.parseLong(userId));
        savedAdvertisement = advertisementRepository.save(savedAdvertisement);

        List<AdvertisementPhoto> advertisementPhotos = uploadPhotosAndLinkToAdvertisement(photos, savedAdvertisement, request.subCategoryId());

        String mainPhotoUrl = advertisementPhotos.get(0).getPhotoUrl();
        savedAdvertisement.setMainPhotoUrl(mainPhotoUrl);
        advertisementRepository.save(savedAdvertisement);

        advertisementPhotos.forEach(advertisementPhotoService::createAdvertisementPhoto);

        return StatusResponse.builder()
                .status(HttpStatus.CREATED.value())
                .data(savedAdvertisement)
                .build();
    }

    private List<AdvertisementPhoto> uploadPhotosAndLinkToAdvertisement(MultipartFile[] photos,
                                                                        Advertisement advertisement,
                                                                        Long subCategoryId) throws IOException {
        String subCategoryName = subCategoryService.findById(subCategoryId).getSubCategoryName();
        List<AdvertisementPhoto> advertisementPhotos = advertisementPhotoService.handlePhotoUpload(photos, subCategoryName);

        advertisementPhotos.forEach(photo -> photo.setAdvertisement(advertisement));
        return advertisementPhotos;
    }

    public StatusResponse getAdvertisement(Long id, HttpServletRequest request) {
        Advertisement advertisement = findById(id);
        List<String> photoUrls = advertisementPhotoService.getPhotoUrlsByAdvertisementId(advertisement.getId());

        AdvertisementDto advertisementDTO = advertisementMapper.mapToDto(advertisement, photoUrls);
        advertisementDTO.setUserDto(userService.getShortUserInfo(request, advertisement.getUserId()));

        return StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(advertisementDTO)
                .build();
    }

    public void deleteAdvertisement(Long id) throws IOException {
        Advertisement advertisement = findById(id);

        advertisementPhotoService.deletePhotosByAdvertisementId(advertisement.getId());
        advertisementRepository.delete(advertisement);

    }

    public StatusResponse setAdvertisementEnabledStatus(Long id) {
        Advertisement advertisement = findById(id);
        boolean currentStatus = advertisement.getIsEnabled();
        advertisement.setIsEnabled(!currentStatus);
        advertisement.setUpdateDate(LocalDateTime.now());
        advertisementRepository.save(advertisement);

        return StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(new AdvertisementStatusResponse(
                        advertisement.getId(),
                        advertisement.getIsEnabled(),
                        advertisement.getUpdateDate()))
                .build();
    }


    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Advertisement not found"));
    }

    public List<Advertisement> findAllUserAdvertisement(Long userId) {
        return advertisementRepository.findByUserId(userId);
    }

    public List<Advertisement> getUserAdvertisementsByEnabledStatus(Long userId, boolean isEnabled) {
        return findAllUserAdvertisement(userId)
                .stream()
                .filter(ad -> ad.getIsEnabled() == isEnabled)
                .toList();
    }
}
