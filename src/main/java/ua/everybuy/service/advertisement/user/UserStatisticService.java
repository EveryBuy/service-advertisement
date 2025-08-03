package ua.everybuy.service.advertisement.user;

import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.response.AdvertisementWithStatisticResponse;
import java.util.List;

/**
 * Provides access to a user's own advertisements with detailed statistics.
 * Used in personal account/profile sections to show performance metrics.
 */
public interface UserStatisticService {
    List<AdvertisementWithStatisticResponse> getUserAdvertisementsWithStatistic(Long userId, boolean isEnabled,
                                                                                Advertisement.AdSection section,
                                                                                int page, int size);
}
