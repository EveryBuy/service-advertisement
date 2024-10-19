package ua.everybuy.buisnesslogic.service.advertisement.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.category.CategoryService;
import ua.everybuy.buisnesslogic.service.category.LowLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.location.RegionService;

@Service
@RequiredArgsConstructor
public class FilterValidator {
    private final RegionService regionService;
    private final TopLevelSubCategoryService topLevelSubCategoryService;
    private final LowLevelSubCategoryService lowLevelSubCategoryService;
    private final CategoryService categoryService;

    public void validate(Long regionId, Long topSubCategoryId, Long lowSubCategoryId, Long categoryId) {
        if (regionId != null) {
            regionService.findById(regionId);
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
}
