package ua.everybuy.buisnesslogic.service.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.routing.dto.mapper.AdvertisementResponseMapper;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdvertisementService {
    private final AdvertisementManagementService advertisementManagementService;
    private final AdvertisementResponseMapper advertisementResponseMapper;

    public List<AdvertisementWithStatisticResponse> getUserAdvertisementsByEnabledStatus(Long userId, boolean isEnabled) {
        return advertisementManagementService.findAllUserAdvertisement(userId)
                .stream()
                .filter(ad -> ad.getIsEnabled() == isEnabled)
                .map(advertisementResponseMapper::mapToAdvertisementStatisticResponse)
                .toList();
    }
}
