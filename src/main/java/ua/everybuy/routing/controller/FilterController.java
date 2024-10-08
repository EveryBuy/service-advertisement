package ua.everybuy.routing.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.everybuy.buisnesslogic.service.advertisement.FilterService;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ad")
@Validated
public class FilterController {
    private final FilterService filterService;

    @GetMapping("/filter")
    public List<FilteredAdvertisementsResponse> doFilter(
            @RequestParam(required = false) @Valid @Min(0) Double minPrice,
            @RequestParam(required = false) @Valid @Min(0) Double maxPrice,
            @RequestParam(required = false) @Valid String sortOrder,
            @RequestParam(required = false) @Valid Long regionId,
            @RequestParam(required = false) @Valid Long topSubCategoryId,
            @RequestParam(required = false) @Valid Long lowSubCategoryId,
            @RequestParam(required = false) @Valid Long categoryId,
            @RequestParam(required = false) @Valid Advertisement.ProductType productType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return filterService.getFilteredAdvertisements(minPrice, maxPrice, sortOrder,
                regionId, topSubCategoryId, lowSubCategoryId, categoryId, productType, pageable);
    }
}
