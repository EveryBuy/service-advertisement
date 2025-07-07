package ua.everybuy.service.advertisement.search;

import java.io.IOException;

public interface AdvertisementReindexingService {
    String reindexAllAdvertisements() throws IOException;
}
