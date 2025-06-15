package ua.everybuy.routing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.buisnesslogic.service.advertisement.search.ElasticSearchService;
import ua.everybuy.routing.dto.AdvertisementSearchResultDto;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/search")
public class ElasticSearchController {
    private final ElasticSearchService elasticSearchService;

    @GetMapping("")
    public AdvertisementSearchResultDto advancedSearchPost(
            @Valid AdvertisementSearchParametersDto searchParams,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        System.out.println(searchParams);
        return elasticSearchService.searchAdvertisements(searchParams, page,size);
    }

}
