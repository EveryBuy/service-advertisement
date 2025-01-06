package ua.everybuy.buisnesslogic.service.advertisement.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.category.CategoryService;
import ua.everybuy.buisnesslogic.service.category.LowLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.location.CityService;
import ua.everybuy.buisnesslogic.service.location.RegionService;

import static ua.everybuy.errorhandling.message.FilterAdvertisementValidationMessages.INVALID_PAGE_MESSAGE;

@Service
@RequiredArgsConstructor
public class FilterValidator {
    private final RegionService regionService;
    private final CityService cityService;
    private final TopLevelSubCategoryService topLevelSubCategoryService;
    private final LowLevelSubCategoryService lowLevelSubCategoryService;
    private final CategoryService categoryService;

    public void validate(Long regionId, Long cityId, Long topSubCategoryId, Long lowSubCategoryId, Long categoryId) {
        if (regionId != null) {
            regionService.findById(regionId);
        }
        if (cityId != null) {
            cityService.findById(cityId);
        }
        if (regionId != null && cityId != null) {
            cityService.getByIdAndRegionId(cityId, regionId);
        }
        if (topSubCategoryId != null) {
            topLevelSubCategoryService.findById(topSubCategoryId);
        }
        if (lowSubCategoryId != null) {
            lowLevelSubCategoryService.findById(lowSubCategoryId);
        }
        if (categoryId != null) {
            categoryService.findById(categoryId);
        }
    }

    public void validatePageNumber(int page) {
        if (page <= 0) {
            throw new IllegalArgumentException(INVALID_PAGE_MESSAGE);
        }
    }
}
