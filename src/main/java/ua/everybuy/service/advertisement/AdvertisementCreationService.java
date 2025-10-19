package ua.everybuy.service.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.everybuy.service.delivery.DeliveryService;
import ua.everybuy.service.photo.PhotoService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.entity.AdvertisementStatistics;
import ua.everybuy.routing.mapper.AdvertisementResponseMapper;
import ua.everybuy.routing.mapper.AdvertisementToEntityMapper;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;
import ua.everybuy.routing.dto.response.CreateAdvertisementResponse;
import ua.everybuy.routing.dto.response.StatusResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AdvertisementCreationService {
    private final AdvertisementManagementService advertisementManagementService;
    private final AdvertisementResponseMapper advertisementResponseMapper;
    private final AdvertisementToEntityMapper toEntityMapper;
    private final DeliveryService deliveryService;
    private final PhotoService photoService;

    @Transactional
    public StatusResponse<CreateAdvertisementResponse> createAdvertisement(CreateAdvertisementRequest createRequest,
                                                                           MultipartFile[] photos,
                                                                           String userId) throws IOException {

        Advertisement newAdvertisement = createAdvertisementEntity(createRequest, photos, userId);
        CreateAdvertisementResponse advertisementResponse = advertisementResponseMapper
                .mapToAdvertisementCreateResponse(newAdvertisement);
        return new StatusResponse<>(HttpStatus.CREATED.value(), advertisementResponse);
    }

    private Advertisement createAdvertisementEntity(CreateAdvertisementRequest createRequest,
                                                    MultipartFile[] photos,
                                                    String userId) throws IOException {
        Advertisement newAdvertisement = toEntityMapper
                .mapToEntity(createRequest, new AdvertisementStatistics(), Long.parseLong(userId));
        newAdvertisement = advertisementManagementService.saveAdvertisement(newAdvertisement);

        processAdvertisementPhotos(newAdvertisement, photos, createRequest.rotations());
        processDeliveryMethods(newAdvertisement, createRequest.deliveryMethods());

        return newAdvertisement;
    }

    private void processAdvertisementPhotos(Advertisement newAdvertisement, MultipartFile[] photos, List<Byte> rotations) throws IOException {
        List<AdvertisementPhoto> advertisementPhotos = photoService.uploadAndLinkPhotos(photos, newAdvertisement, rotations,
                newAdvertisement.getTopSubCategory().getSubCategoryName());
        advertisementManagementService.setMainPhoto(newAdvertisement, advertisementPhotos);
    }

    private void processDeliveryMethods(Advertisement newAdvertisement, Set<String> deliveryMethods) {
        deliveryService.saveAdvertisementDeliveries(newAdvertisement, deliveryMethods);
    }
}
