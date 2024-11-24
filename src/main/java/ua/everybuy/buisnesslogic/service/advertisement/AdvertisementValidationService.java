package ua.everybuy.buisnesslogic.service.advertisement;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.errorhandling.message.AdvertisementValidationMessages;

@Service
public class AdvertisementValidationService {
    public void validate(Advertisement advertisement) {
        if (advertisement == null) {
            throw new IllegalArgumentException(AdvertisementValidationMessages
                    .ADVERTISEMENT_NULL_MESSAGE);
        }
    }

    public void validateIsActive(Advertisement advertisement) {
        if (!advertisement.getIsEnabled()) {
            throw new AccessDeniedException(AdvertisementValidationMessages
                    .INACTIVE_ADVERTISEMENT_MESSAGE);
        }
    }

    public void validateUserAccess(Advertisement advertisement, Long userId) {
        if (!advertisement.getUserId().equals(userId)) {
            throw new AccessDeniedException(String.format(
                    AdvertisementValidationMessages
                            .ACCESS_DENIED_MESSAGE_TEMPLATE, userId, advertisement.getId()));
        }
    }
}
