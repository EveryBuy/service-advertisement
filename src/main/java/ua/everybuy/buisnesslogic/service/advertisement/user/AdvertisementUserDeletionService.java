package ua.everybuy.buisnesslogic.service.advertisement.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.everybuy.buisnesslogic.service.advertisement.AdvertisementManagementService;
import ua.everybuy.buisnesslogic.service.photo.PhotoService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementPhoto;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.security.SecurityValidationService;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementUserDeletionService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementManagementService advertisementManagementService;
    private final PhotoService photoService;
    private final SecurityValidationService securityValidationService;

    @Transactional
    public void deleteAllAndPushUserAdvertisements(Long userId, HttpServletRequest request) throws IOException {
        securityValidationService.validatePassword(request);

        List<Advertisement> userAds = advertisementRepository.findAllByUserId(userId);
        userAds.forEach(advertisementManagementService::pushAdvertisementChangeToChatService);

        List<AdvertisementPhoto> listAllPhotos = userAds.stream()
                .map(photoService::findPhotosByAdvertisement)
                .flatMap(List::stream)
                .toList();

        photoService.deleteAllPhotos(listAllPhotos);
        advertisementRepository.deleteAll(userAds);
    }
}
