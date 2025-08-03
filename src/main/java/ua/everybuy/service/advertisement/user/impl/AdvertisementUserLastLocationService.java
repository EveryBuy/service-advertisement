package ua.everybuy.service.advertisement.user.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.City;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.service.advertisement.user.UserLastLocationService;
import ua.everybuy.service.location.CityService;

@Service
@RequiredArgsConstructor
public class AdvertisementUserLastLocationService implements UserLastLocationService {
    private final AdvertisementRepository advertisementRepository;
    private final CityService cityService;

    @Override
    public City getLastLocationForUser(Long userId) {
        return cityService.findById(getLastLocationId(userId));
    }

    private Long getLastLocationId(Long userId) {
        return advertisementRepository.findFirstCityIdByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No advertisements found for user ID: "
                                + userId + ", cannot determine last location"));
    }
}
