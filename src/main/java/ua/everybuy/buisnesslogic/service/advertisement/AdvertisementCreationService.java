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
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;
import ua.everybuy.routing.dto.response.CreateAdvertisementResponse;
import ua.everybuy.routing.dto.response.StatusResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AdvertisementCreationService {
    private final AdvertisementService advertisementService;
    private final AdvertisementSubCategoryService advertisementSubCategoryService;
    private final PhotoService photoService;
    private final AdvertisementMapper advertisementMapper;
    private final DeliveryService deliveryService;

    public StatusResponse<CreateAdvertisementResponse> createAdvertisement(CreateAdvertisementRequest createRequest,
                                                                           MultipartFile[] photos,
                                                                           String userId) throws IOException {

        Advertisement newAdvertisement = createAndSaveAdvertisement(createRequest, userId);

        // Save the associated photos and update the main photo URL
        advertisementService.saveAdvertisementPhotos(photos, newAdvertisement, createRequest.topSubCategoryId());
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
        return advertisementService.saveAdvertisement(advertisement);
    }
}
