package ua.everybuy.service.advertisement.user;

import jakarta.persistence.EntityNotFoundException;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.UserAdvertisementDto;

/**
 * Used when browsing seller profiles to display their active listings with filtering capabilities.
 */
public interface UserProfileService {
    /**
     * Retrieves paginated and filtered advertisements for a user's profile page
     * @param userId ID of the user/seller to view
     * @param categoryId Optional category filter (null for all categories)
     * @param section Advertisement section (BUY/SELL)
     * @param page Page number (1-based)
     * @param size Number of items per page
     * @return UserAdvertisementDto containing:
     *         - Filtered advertisements
     *         - Total advertisement count
     *         - Category statistics
     * @throws EntityNotFoundException if user or category doesn't exist
     */
    UserAdvertisementDto getUserActiveFilteredAdvertisements(Long userId, Long categoryId,
                                                             Advertisement.AdSection section,
                                                             int page, int size);
}
