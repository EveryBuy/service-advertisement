package ua.everybuy.buisnesslogic.service.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.photo.PhotoService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.routing.dto.mapper.AdvertisementResponseMapper;
import ua.everybuy.routing.dto.mapper.AdvertisementToEntityMapper;
import ua.everybuy.routing.dto.request.UpdateAdvertisementRequest;
import ua.everybuy.routing.dto.response.StatusResponse;
import ua.everybuy.routing.dto.response.UpdateAdvertisementResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AdvertisementUpdateService {
    private final AdvertisementManagementService advertisementManagementService;
    private final AdvertisementResponseMapper advertisementResponseMapper;
    private final AdvertisementToEntityMapper toEntityMapper;
    private final PhotoService photoService;
    private final DeliveryService deliveryService;

    @Transactional
    public StatusResponse<UpdateAdvertisementResponse> updateAdvertisement(Long advertisementId,
                                                                           UpdateAdvertisementRequest updateRequest,
                                                                           MultipartFile[] newPhotos,
                                                                           String userId) throws IOException {

        Advertisement updatedAdvertisement = updateAdvertisementEntity(advertisementId,
                updateRequest, newPhotos, userId);

        UpdateAdvertisementResponse updateAdvertisementResponse = advertisementResponseMapper
                .mapToAdvertisementUpdateResponse(updatedAdvertisement);
        return new StatusResponse<>(HttpStatus.OK.value(), updateAdvertisementResponse);
    }

    private Advertisement updateAdvertisementEntity(Long advertisementId,
                                                    UpdateAdvertisementRequest updateRequest,
                                                    MultipartFile[] photos,
                                                    String userId) throws IOException {

        Advertisement existingAdvertisement = advertisementManagementService
                .findAdvertisementByIdAndUserId(advertisementId, Long.parseLong(userId));

        existingAdvertisement = toEntityMapper.mapToEntity(updateRequest, existingAdvertisement);
        existingAdvertisement = advertisementManagementService.saveAdvertisement(existingAdvertisement);

        processAdvertisementPhotos(existingAdvertisement, photos);

        processDeliveryMethods(existingAdvertisement, updateRequest.deliveryMethods());

        advertisementManagementService.pushAdvertisementChangeToChat(existingAdvertisement);
        return existingAdvertisement;
    }

    private void processAdvertisementPhotos(Advertisement advertisement,
                                            MultipartFile[] photos) throws IOException {
        photoService.deletePhotosByAdvertisementId(advertisement);
        List<AdvertisementPhoto> advertisementPhotos = photoService.uploadAndLinkPhotos(photos, advertisement,
                advertisement.getTopSubCategory().getSubCategoryName());
        advertisementManagementService.updateMainPhoto(advertisement, advertisementPhotos);
    }

    private void processDeliveryMethods(Advertisement advertisement, Set<String> deliveryMethods) {
        deliveryService.updateAdvertisementDeliveries(advertisement, deliveryMethods);
    }

}
