package ua.everybuy.service.advertisement.search.filters.processors;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Component;
import ua.everybuy.service.advertisement.search.filters.CategoryFilter;
import ua.everybuy.service.advertisement.search.filters.CityFilter;
import ua.everybuy.service.advertisement.search.filters.EnabledFilter;
import ua.everybuy.service.advertisement.search.filters.KeywordFilter;
import ua.everybuy.service.advertisement.search.filters.LowSubCategoryFilter;
import ua.everybuy.service.advertisement.search.filters.PriceFilter;
import ua.everybuy.service.advertisement.search.filters.ProductTypeFilter;
import ua.everybuy.service.advertisement.search.filters.RegionFilter;
import ua.everybuy.service.advertisement.search.filters.SectionFilter;
import ua.everybuy.service.advertisement.search.filters.TopSubCategoryFilter;
import ua.everybuy.routing.dto.PriceRangeDto;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

@RequiredArgsConstructor
@Component
public class FilterAdvertisementProcessor {
    private final CategoryFilter categoryFilter;
    private final CityFilter cityFilter;
    private final EnabledFilter enabledFilter;
    private final KeywordFilter keywordFilter;
    private final LowSubCategoryFilter lowSubCategoryFilter;
    private final PriceFilter priceFilter;
    private final ProductTypeFilter productTypeFilter;
    private final RegionFilter regionFilter;
    private final SectionFilter sectionFilter;
    private final TopSubCategoryFilter topSubCategoryFilter;

    public void process(BoolQueryBuilder query, AdvertisementSearchParametersDto dto) {
        categoryFilter.apply(query, dto.getCategoryId());
        cityFilter.apply(query, dto.getCityId());
        enabledFilter.apply(query, new Object());
        keywordFilter.apply(query, dto.getKeyword());
        lowSubCategoryFilter.apply(query, dto.getLowSubCategoryId());
        priceFilter.apply(query, new PriceRangeDto(dto.getMinPrice(), dto.getMaxPrice()));
        productTypeFilter.apply(query, dto.getProductType());
        regionFilter.apply(query, dto.getRegionId());
        sectionFilter.apply(query, dto.getSection());
        topSubCategoryFilter.apply(query, dto.getTopSubCategoryId());
    }
}
