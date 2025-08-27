package ua.everybuy.service.advertisement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.everybuy.service.integration.ChatService;
import ua.everybuy.service.photo.PhotoService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.mapper.AdvertisementResponseMapper;
import ua.everybuy.routing.mapper.AdvertisementToDtoMapper;
import ua.everybuy.routing.dto.response.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementManagementService {
    private final AdvertisementStorageService advertisementStorageService;
    private final AdvertisementValidationService advertisementValidationService;
    private final PhotoService photoService;
    private final StatisticsService statisticsService;
    private final AdvertisementResponseMapper responseMapper;
    private final AdvertisementToDtoMapper dtoMapper;
    private final ChatService chatService;

    public Advertisement saveAdvertisement(Advertisement advertisement) {
        advertisementValidationService.validate(advertisement);
        return advertisementStorageService.save(advertisement);
    }

    public void setMainPhoto(Advertisement advertisement, List<AdvertisementPhoto> photos) {
        if (photos != null && !photos.isEmpty()) {
            String mainPhotoUrl = photos.get(0).getPhotoUrl();
            advertisement.setMainPhotoUrl(mainPhotoUrl);
            saveAdvertisement(advertisement);
        }
    }

    public Advertisement findActiveAdvertisementById(Long id) {
        return advertisementStorageService.findActiveById(id);
    }

    public StatusResponse<AdvertisementDto> getActiveAdvertisement(Long id) {
        Advertisement advertisement = findActiveAdvertisementById(id);
        statisticsService.incrementViewsAndSave(advertisement);
        return buildStatusResponse(HttpStatus.OK, advertisement, dtoMapper::mapToDto);
    }

    public StatusResponse<AdvertisementDto> retrieveAdvertisementWithAuthorization(Long id, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        Advertisement advertisement = advertisementStorageService.findById(id);

        if (advertisement.getIsEnabled()) {
            return buildStatusResponse(HttpStatus.OK, advertisement, dtoMapper::mapToDto);
        }

        advertisementValidationService.validateUserAccess(advertisement, userId);
        return buildStatusResponse(HttpStatus.OK, advertisement, dtoMapper::mapToDto);
    }

    public void deleteAdvertisement(Long advertisementId, Principal principal) throws IOException {
        Advertisement advertisement = advertisementStorageService
                .findAdvertisementByIdAndUserId(advertisementId,
                        Long.parseLong(principal.getName()));
        photoService.deletePhotosByAdvertisementId(advertisement);
        advertisement.setIsEnabled(false);
        pushAdvertisementChangeToChatService(advertisement);

        advertisementStorageService.delete(advertisement);
    }

    public StatusResponse<AdvertisementStatusResponse> setAdvertisementEnabledStatus(Long id) {
        Advertisement advertisement = advertisementStorageService.findById(id);
        toggleAdvertisementStatus(advertisement);
        return buildStatusResponse(HttpStatus.OK, advertisement, responseMapper::mapToAdvertisementStatusResponse);
    }

    private void toggleAdvertisementStatus(Advertisement advertisement) {
        advertisement.setIsEnabled(!advertisement.getIsEnabled());
        advertisement.setUpdateDate(LocalDateTime.now());
        saveAdvertisement(advertisement);
        pushAdvertisementChangeToChatService(advertisement);
        log.info("Advertisement status toggled and sent to chat service");
    }

    public AdvertisementInfoForChatService getAdvertisementShortInfo(Long advertisementId) {
        Advertisement advertisement = advertisementStorageService.findById(advertisementId);
        return responseMapper.mapToAdvertisementInfoForChatService(advertisement);
    }

    public void pushAdvertisementChangeToChatService(Advertisement advertisement) {
        AdvertisementInfoForChatService advertisementInfoForChatService = responseMapper
                .mapToAdvertisementInfoForChatService(advertisement);
        chatService.sendInfoAboutChange(advertisementInfoForChatService);
    }

    private <T> StatusResponse<T> buildStatusResponse(HttpStatus status, Advertisement advertisement,
                                                      Function<Advertisement, T> mapper) {
        T dto = mapper.apply(advertisement);
        return new StatusResponse<>(status.value(), dto);
    }

}
