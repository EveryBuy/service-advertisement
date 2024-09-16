package ua.everybuy.routing.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ua.everybuy.buisnesslogic.service.CategoryService;
import ua.everybuy.buisnesslogic.service.CityService;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.entity.City;

@Component
@RequiredArgsConstructor
public class AdvertisementMappingHelper {
    private final CityService cityService;
    private final CategoryService categoryService;

    @Named("cityIdToCity")
    public City cityIdToCity(Long cityId) {
        return cityService.findById(cityId);
    }
    @Named("categoryIdToCategory")
    public Category categoryIdToSubCategory(Long categoryId) {
        return categoryService.findById(categoryId);
    }

    @Named("truncateDescription")
    public String truncateDescription(String description) {
        if (description != null && description.length() > 200) {
            return description.substring(0, 200);
        }
        return description;
    }
}
