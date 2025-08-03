package ua.everybuy.service.advertisement.user;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * Service handling complete advertisement cleanup when a user account is deleted.
 * Performs cascading deletion of all user advertisement assets.
 */
public interface UserDeletionService {
    void deleteAllAndPushUserAdvertisements(Long userId, HttpServletRequest request) throws IOException;
}
