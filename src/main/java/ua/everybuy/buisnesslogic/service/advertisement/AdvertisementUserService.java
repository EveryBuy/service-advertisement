package ua.everybuy.buisnesslogic.service.advertisement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.errorhandling.message.AdvertisementValidationMessages;
import ua.everybuy.routing.dto.mapper.AdvertisementResponseMapper;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementUserService {
    private final AdvertisementStorageService advertisementStorageService;
    private final AdvertisementResponseMapper advertisementResponseMapper;

    public List<AdvertisementWithStatisticResponse> getUserAdvertisementsByStatus(Long userId, boolean isEnabled) {
        return getAdvertisementsByUserId(userId).stream()
                .filter(ad -> ad.getIsEnabled() == isEnabled)
                .map(advertisementResponseMapper::mapToAdvertisementStatisticResponse)
                .toList();
    }

    public List<Advertisement> getAdvertisementsByUserId(Long userId) {
        List<Advertisement> userAdvertisement = advertisementStorageService.findByUserId(userId);
        if (userAdvertisement == null || userAdvertisement.isEmpty()) {
            throw new EntityNotFoundException(AdvertisementValidationMessages
                    .NO_ADVERTISEMENTS_FOUND_MESSAGE + userId);
        }
        return userAdvertisement;
    }
}
