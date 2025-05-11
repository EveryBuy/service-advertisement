package ua.everybuy.buisnesslogic.service.location;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.City;
import ua.everybuy.database.repository.location.CityRepository;

import java.util.List;

import static ua.everybuy.errorhandling.message.CityValidationMessages.CITY_NOT_FOUND_BY_REGION_MESSAGE;
import static ua.everybuy.errorhandling.message.CityValidationMessages.CITY_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityService {
    private final CityRepository cityRepository;
    private final RegionService regionService;

    @Cacheable(value = "citiesCache")
    public List<City> getAllCitiesWithRegions() {
        log.info("[CACHE INFO] Fetching all cities with regions - Cache START");
        return cityRepository.findAllWithRegions();
    }

    @Cacheable(value = "citiesByRegionCache", key = "#regionId")
    public List<City> getCitiesByRegionId(Long regionId) {
        log.info("[CACHE INFO] Fetching cities for regionId: {} - Cache START", regionId);
        regionService.findById(regionId);
        return cityRepository.findAllByRegionId(regionId);
    }

    @Cacheable(value = "cityCache", key = "#id")
    public City findById(Long id) {
        return cityRepository.findByIdWithRegion(id)
                .orElseThrow(() -> new EntityNotFoundException(CITY_NOT_FOUND_MESSAGE + id));
    }

    @Cacheable(value = "cityByRegionCache", key = "{#id, #regionId}")
    public City getByIdAndRegionId(Long id, Long regionId) {
        return cityRepository.findByCityIdAndRegionId(id, regionId)
                .orElseThrow(() -> new EntityNotFoundException(CITY_NOT_FOUND_BY_REGION_MESSAGE + regionId));
    }
}
