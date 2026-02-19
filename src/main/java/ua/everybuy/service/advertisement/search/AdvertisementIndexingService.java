package ua.everybuy.service.advertisement.search;

import ua.everybuy.database.entity.Advertisement;

import java.io.IOException;

public interface AdvertisementIndexingService {
    String reindexAllAdvertisements() throws IOException;
    void indexAdvertisement(Advertisement advertisement);
    void deleteFromIndex(Long id);
}
