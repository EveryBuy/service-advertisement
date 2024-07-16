package ua.everybuy.routing.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ua.everybuy.buisnesslogic.service.CityService;
import ua.everybuy.buisnesslogic.service.SubCategoryService;
import ua.everybuy.database.entity.City;
import ua.everybuy.database.entity.SubCategory;

@Component
@RequiredArgsConstructor
public class AdvertisementMappingHelper {

    private final CityService cityService;

    private final SubCategoryService subCategoryService;

    @Named("cityIdToCity")
    public City cityIdToCity(Long cityId) {
        return cityService.findById(cityId);
    }

    @Named("subCategoryIdToSubCategory")
    public SubCategory subCategoryIdToSubCategory(Long subCategoryId) {
        return subCategoryService.findById(subCategoryId);
    }
    @Named("truncateDescription")
    public String truncateDescription(String description) {
        if (description != null && description.length() > 200) {
            return description.substring(0, 200);
        }
        return description;
    }
}
