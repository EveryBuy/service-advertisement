package ua.everybuy.buisnesslogic.service.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.buisnesslogic.service.category.AdvertisementSubCategoryService;
import ua.everybuy.buisnesslogic.service.photo.PhotoService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.mapper.AdvertisementMapper;
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
    private final AdvertisementSubCategoryService advertisementSubCategoryService;
    private final PhotoService photoService;
    private final AdvertisementMapper advertisementMapper;
    private final DeliveryService deliveryService;
    public StatusResponse<UpdateAdvertisementResponse> updateAdvertisement(Long advertisementId,
                                                                           UpdateAdvertisementRequest updateRequest,
                                                                           MultipartFile[] newPhotos,
                                                                           String userId) throws IOException {

        Advertisement existingAdvertisement = advertisementManagementService.findAdvertisementByIdAndUserId(advertisementId, Long.parseLong(userId));
        photoService.deletePhotosByAdvertisementId(existingAdvertisement.getId());

        TopLevelSubCategory topLevelSubCategory = advertisementSubCategoryService.getTopLevelSubCategory(updateRequest);
        LowLevelSubCategory lowLevelSubCategory = advertisementSubCategoryService.getLowLevelSubCategory(updateRequest);

        existingAdvertisement = advertisementMapper.mapToEntity(updateRequest, existingAdvertisement,
                topLevelSubCategory, lowLevelSubCategory);

        advertisementManagementService.uploadAndSaveAdvertisementPhotos(newPhotos, existingAdvertisement, updateRequest.topSubCategoryId());
        List<String> updatedPhotos = photoService.getPhotoUrlsByAdvertisementId(existingAdvertisement.getId());

        deliveryService.updateAdvertisementDeliveries(existingAdvertisement, updateRequest.deliveryMethods());
        Set<String> deliveryMethods = deliveryService.getAdvertisementDeliveryMethods(existingAdvertisement);

        UpdateAdvertisementResponse updateAdvertisementResponse = advertisementMapper
                .mapToAdvertisementUpdateResponse(existingAdvertisement, deliveryMethods, updatedPhotos);
        return new StatusResponse<>(HttpStatus.OK.value(), updateAdvertisementResponse);
    }
}
