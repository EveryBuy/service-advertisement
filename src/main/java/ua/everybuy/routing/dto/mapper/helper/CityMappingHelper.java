package ua.everybuy.routing.dto.mapper.helper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ua.everybuy.buisnesslogic.service.location.CityService;
import ua.everybuy.database.entity.City;

@Component
@RequiredArgsConstructor
public class CityMappingHelper {
    private final CityService cityService;

    @Named("cityIdToCity")
    public City cityIdToCity(Long cityId) {
        return cityService.findById(cityId);
    }
}
