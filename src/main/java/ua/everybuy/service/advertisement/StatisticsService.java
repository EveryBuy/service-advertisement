package ua.everybuy.service.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementStatistics;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;

/**
 * Service responsible for managing advertisement statistics.
 * This service provides methods to increment and persist various statistical metrics
 * for advertisements, such as view counts and favorite counts.
 *
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final AdvertisementRepository advertisementRepository;

    public void incrementViewsAndSave(Advertisement advertisement) {
        if (advertisement != null) {
            AdvertisementStatistics statistics = advertisement.getStatistics();
            statistics.incrementViews();
            advertisementRepository.save(advertisement);
        }
    }

    public void incrementFavouriteCountAndSave(Advertisement advertisement) {
        if (advertisement != null) {
            AdvertisementStatistics statistics = advertisement.getStatistics();
            statistics.incrementFavouriteCount();
            advertisementRepository.save(advertisement);
        }
    }
}
