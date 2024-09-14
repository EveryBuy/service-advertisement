package ua.everybuy.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementStatistics;
import ua.everybuy.database.repository.AdvertisementRepository;

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
