package ua.everybuy.service.advertisement.search;

import ua.everybuy.database.entity.Advertisement;

public interface AdvertisementIndexingService {
    void indexAdvertisement(Advertisement advertisement);
    void deleteFromIndex(Long id);
}
