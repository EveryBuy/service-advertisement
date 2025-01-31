package ua.everybuy.routing.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.everybuy.buisnesslogic.service.advertisement.filter.AdvertisementFilterService;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ad")
@Validated
public class FilterController {
    private final AdvertisementFilterService advertisementFilterService;

    @GetMapping("/filter")
    public FilteredAdvertisementsResult doFilter(
            @RequestParam(required = false) @Valid @Min(0) Double minPrice,
            @RequestParam(required = false) @Valid @Min(0) Double maxPrice,
            @RequestParam(required = false) @Valid String sortOrder,
            @RequestParam(required = false) @Valid Long regionId,
            @RequestParam(required = false) @Valid Long cityId,
            @RequestParam(required = false) @Valid Long topSubCategoryId,
            @RequestParam(required = false) @Valid Long lowSubCategoryId,
            @RequestParam(required = false) @Valid Long categoryId,
            @RequestParam(required = false) @Valid Advertisement.ProductType productType,
            @RequestParam(required = false, defaultValue = "SELL") @Valid Advertisement.AdSection section,
            @RequestParam(required = false) @Valid String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return advertisementFilterService.getFilteredAdvertisements(minPrice, maxPrice, sortOrder,
                regionId, cityId, topSubCategoryId, lowSubCategoryId, categoryId, productType, section, keyword, page, size);
    }
}
