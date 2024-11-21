package ua.everybuy.buisnesslogic.service.advertisement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.integration.ChatService;
import ua.everybuy.buisnesslogic.service.photo.PhotoService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.AdvertisementRepository;
import ua.everybuy.errorhandling.message.AdvertisementValidationMessages;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.mapper.AdvertisementResponseMapper;
import ua.everybuy.routing.dto.mapper.AdvertisementToDtoMapper;
import ua.everybuy.routing.dto.response.*;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementManagementService {
    private final AdvertisementRepository advertisementRepository;
    private final PhotoService photoService;
    private final StatisticsService statisticsService;
    private final AdvertisementResponseMapper responseMapper;
    private final AdvertisementToDtoMapper dtoMapper;
    private final ChatService chatService;

    public Advertisement saveAdvertisement(Advertisement advertisement) {
        validateAdvertisement(advertisement);
        return advertisementRepository.save(advertisement);
    }

    public void updateMainPhoto(Advertisement existingAdvertisement, List<AdvertisementPhoto> photos) {
        if (photos != null && !photos.isEmpty()) {
            String mainPhotoUrl = photos.get(0).getPhotoUrl();
            existingAdvertisement.setMainPhotoUrl(mainPhotoUrl);
            saveAdvertisement(existingAdvertisement);
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
        return new StatusResponse<>(HttpStatus.OK.value(), dtoMapper.mapToDto(advertisement));
    }

    public StatusResponse<AdvertisementDto> retrieveAdvertisementWithAuthorization(Long id, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        Advertisement advertisement = findById(id);

        if (advertisement.getIsEnabled()) {
            AdvertisementDto advertisementDTO = dtoMapper.mapToDto(advertisement);
            return new StatusResponse<>(HttpStatus.OK.value(), advertisementDTO);
        }

        validateUserAccessToAdvertisement(advertisement, userId);
        AdvertisementDto advertisementDTO = dtoMapper.mapToDto(advertisement);
        return new StatusResponse<>(HttpStatus.OK.value(), advertisementDTO);
    }

    public void deleteAdvertisement(Long advertisementId, Principal principal) throws IOException {
        Advertisement advertisement = findAdvertisementByIdAndUserId(advertisementId,
                Long.parseLong(principal.getName()));
        photoService.deletePhotosByAdvertisementId(advertisement);
        advertisement.setIsEnabled(false);
        pushAdvertisementChangeToChat(advertisement);
        advertisementRepository.delete(advertisement);
    }

    public StatusResponse<AdvertisementStatusResponse> setAdvertisementEnabledStatus(Long id) {
        Advertisement advertisement = findById(id);
        toggleAdvertisementStatus(advertisement);
        return new StatusResponse<>(HttpStatus.OK.value(),
                responseMapper.mapToAdvertisementStatusResponse(advertisement));
    }

    private void toggleAdvertisementStatus(Advertisement advertisement) {
        boolean currentStatus = advertisement.getIsEnabled();
        advertisement.setIsEnabled(!currentStatus);
        advertisement.setUpdateDate(LocalDateTime.now());
        saveAdvertisement(advertisement);
        pushAdvertisementChangeToChat(advertisement);
    }

    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AdvertisementValidationMessages
                        .ADVERTISEMENT_NOT_FOUND_MESSAGE));
    }

    public List<Advertisement> findAllUserAdvertisement(Long userId) {
        List<Advertisement> userAdvertisement = advertisementRepository.findByUserId(userId);
        if (userAdvertisement == null || userAdvertisement.isEmpty()) {
            throw new EntityNotFoundException(AdvertisementValidationMessages
                    .NO_ADVERTISEMENTS_FOUND_MESSAGE + userId);
        }
        return userAdvertisement;
    }

    public Advertisement findAdvertisementByIdAndUserId(Long advertisementId, Long userId) {
        return advertisementRepository.findByIdAndUserId(advertisementId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(AdvertisementValidationMessages
                                .ACCESS_DENIED_MESSAGE_TEMPLATE, userId, advertisementId)));
    }

    public List<Advertisement> getActiveAdvertisements() {
        return advertisementRepository.findByIsEnabledTrueOrderByCreationDateDesc();
    }

    public AdvertisementInfoForChatService getAdvertisementShortInfo(Long advertisementId) {
        Advertisement advertisement = findById(advertisementId);
        return responseMapper.mapToAdvertisementInfoForChatService(advertisement);
    }

    public void pushAdvertisementChangeToChat(Advertisement advertisement) {
        AdvertisementInfoForChatService advertisementInfoForChatService = responseMapper.mapToAdvertisementInfoForChatService(advertisement);
        chatService.sendInfoAboutChange(advertisementInfoForChatService);
    }

    private void validateAdvertisement(Advertisement advertisement) {
        if (advertisement == null) {
            throw new IllegalArgumentException(AdvertisementValidationMessages
                    .ADVERTISEMENT_NULL_MESSAGE);
        }
    }

    private void validateAdvertisementIsActive(Advertisement advertisement) {
        if (!advertisement.getIsEnabled()) {
            throw new AccessDeniedException(AdvertisementValidationMessages
                    .INACTIVE_ADVERTISEMENT_MESSAGE);
        }
    }

    private void validateUserAccessToAdvertisement(Advertisement advertisement, Long userId) {
        if (!advertisement.getUserId().equals(userId)) {
            throw new AccessDeniedException(String.format(AdvertisementValidationMessages
                            .ACCESS_DENIED_MESSAGE_TEMPLATE,
                    userId, advertisement.getId()));
        }
    }
}
