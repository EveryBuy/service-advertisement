package ua.everybuy.buisnesslogic.service.advertisement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.errorhandling.message.AdvertisementValidationMessages;
import ua.everybuy.routing.dto.mapper.AdvertisementResponseMapper;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvertisementUserService {
    private final AdvertisementStorageService advertisementStorageService;
    private final AdvertisementResponseMapper advertisementResponseMapper;

    public List<AdvertisementWithStatisticResponse> getUserAdvertisements(Long userId, boolean isEnabled,
                                                                          Advertisement.AdSection section,
                                                                          int page, int size) {
        List<Advertisement> advertisements = findUserAdvertisements(userId, section, page, size, isEnabled);

        return advertisements.stream()
                .map(advertisementResponseMapper::mapToAdvertisementStatisticResponse)
                .toList();
    }

    private List<Advertisement> findUserAdvertisements(Long userId, Advertisement.AdSection section,
                                                       int page, int size, Boolean isEnabled) {
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        List<Advertisement> advertisements = advertisementStorageService.findByUserId(userId, section, isEnabled, pageable);

        return advertisements;
    }
}
