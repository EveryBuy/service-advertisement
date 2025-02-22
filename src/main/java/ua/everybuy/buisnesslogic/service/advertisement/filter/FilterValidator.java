package ua.everybuy.buisnesslogic.service.advertisement.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.category.CategoryService;
import ua.everybuy.buisnesslogic.service.category.LowLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.location.CityService;
import ua.everybuy.buisnesslogic.service.location.RegionService;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

@Service
@RequiredArgsConstructor
public class FilterValidator {
    private final RegionService regionService;
    private final CityService cityService;
    private final TopLevelSubCategoryService topLevelSubCategoryService;
    private final LowLevelSubCategoryService lowLevelSubCategoryService;
    private final CategoryService categoryService;

    public void validate(AdvertisementSearchParametersDto searchParametersDto) {
        if (searchParametersDto.getRegionId() != null) {
            regionService.findById(searchParametersDto.getRegionId());
        }
        if (searchParametersDto.getCityId() != null) {
            cityService.findById(searchParametersDto.getCityId());
        }
        if (searchParametersDto.getRegionId() != null && searchParametersDto.getCityId() != null) {
            cityService.getByIdAndRegionId(searchParametersDto.getCityId(), searchParametersDto.getRegionId());
        }
        if (searchParametersDto.getTopSubCategoryId() != null) {
            topLevelSubCategoryService.findById(searchParametersDto.getTopSubCategoryId());
        }
        if (searchParametersDto.getLowSubCategoryId() != null) {
            lowLevelSubCategoryService.findById(searchParametersDto.getLowSubCategoryId());
        }
        if (searchParametersDto.getCategoryId() != null) {
            categoryService.findById(searchParametersDto.getCategoryId());
        }
    }
}
