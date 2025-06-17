package ua.everybuy.routing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.everybuy.service.advertisement.filter.FilterAdvertisementService;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;
import ua.everybuy.routing.dto.AdvertisementSearchResultDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Validated
public class FilterController {
    private final FilterAdvertisementService filterAdvertisementService;

    @GetMapping("/filter")
    public AdvertisementSearchResultDto doFilter(
            @Valid AdvertisementSearchParametersDto searchParametersDto,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return filterAdvertisementService.getFilteredAdvertisements(searchParametersDto, page, size);
    }
}
