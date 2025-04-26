package ua.everybuy.buisnesslogic.service.advertisement.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.advertisement.AdvertisementStorageService;
import ua.everybuy.buisnesslogic.strategy.sort.SortStrategyFactory;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.mapper.AdvertisementResponseMapper;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementUserStatisticService {
    private final AdvertisementStorageService advertisementStorageService;
    private final AdvertisementResponseMapper advertisementResponseMapper;
    private final SortStrategyFactory sortStrategyFactory;

    public List<AdvertisementWithStatisticResponse> getUserAdvertisementsWithStatistic(Long userId, boolean isEnabled,
                                                                                       Advertisement.AdSection section,
                                                                                       int page, int size) {
        List<Advertisement> advertisements = findUserAdvertisements(userId, isEnabled, section, page, size);

        return advertisements.stream()
                .map(advertisementResponseMapper::mapToAdvertisementStatisticResponse)
                .toList();
    }

    private List<Advertisement> findUserAdvertisements(Long userId, Boolean isEnabled,
                                                       Advertisement.AdSection section,
                                                       int page, int size) {
        Pageable pageable = createPageable(page, size);
        return advertisementStorageService.findByUserId(userId, isEnabled, section, pageable);
    }

    private Pageable createPageable(int page, int size) {
        Sort sort = sortStrategyFactory.getSortStrategy(SortStrategyFactory.DATE_DESCENDING).getSortOrder();
        return PageRequest.of(page - 1, size, sort);
    }

}
