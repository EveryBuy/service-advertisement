package ua.everybuy.service.advertisement.user;

import jakarta.persistence.EntityNotFoundException;
import ua.everybuy.database.entity.City;

public interface UserLastLocationService {
    /**
     * Retrieves the last city a user selected when creating an advertisement
     * @param userId the ID of the user to check
     * @return the City entity of the last used location
     * @throws EntityNotFoundException if user has no advertisement history
     */
    City getLastLocationForUser(Long userId);

}
