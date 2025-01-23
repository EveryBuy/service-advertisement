package ua.everybuy.buisnesslogic.service.advertisement.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.strategy.sort.SortStrategyFactory;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.AdvertisementRepository;
import ua.everybuy.routing.dto.PriceRangeDto;
import ua.everybuy.routing.dto.mapper.AdvertisementFilterMapper;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResult;

import java.util.List;
import java.util.stream.Collectors;

import static ua.everybuy.buisnesslogic.strategy.sort.SortStrategyFactory.DATE_DESCENDING;

@Service
@RequiredArgsConstructor
public class AdvertisementFilterService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementFilterMapper advertisementFilterMapper;
    private final FilterAdvertisementSpecificationFactory filterAdSpecFactory;
    private final SortStrategyFactory sortStrategyFactory;
    private final FilterValidator filterValidator;

    public FilteredAdvertisementsResult getFilteredAdvertisements(Double minPrice, Double maxPrice,
                                                                  String sortOrder, Long regionId, Long cityId,
                                                                  Long topSubCategoryId, Long lowSubCategoryId,
                                                                  Long categoryId, Advertisement.ProductType productType,
                                                                  Advertisement.AdSection section, String keyword,
                                                                  int page, int size) {

        Page<Advertisement> filteredAds = applyFilters(minPrice, maxPrice, sortOrder,
                regionId, cityId, topSubCategoryId, lowSubCategoryId, categoryId, productType, section, keyword, page, size);

        long totalAdvertisements = filteredAds.getTotalElements();
        int totalPages = filteredAds.getTotalPages();

        PriceRangeDto priceRange = getPriceRange(cityId, regionId, categoryId, topSubCategoryId, lowSubCategoryId, productType, section);
        List<FilteredAdvertisementsResponse> advertisements = mapToResponse(filteredAds);

        return buildResult(advertisements, totalAdvertisements, totalPages, priceRange);

    }

    private Page<Advertisement> applyFilters(Double minPrice, Double maxPrice, String sortOrder,
                                             Long regionId, Long cityId, Long topSubCategoryId, Long lowSubCategoryId,
                                             Long categoryId, Advertisement.ProductType productType,
                                             Advertisement.AdSection section, String keyword, int page, int size) {

        filterValidator.validatePageNumber(page);
        filterValidator.validate(regionId, cityId, topSubCategoryId, lowSubCategoryId, categoryId);

        Specification<Advertisement> specs = filterAdSpecFactory.createSpecification(minPrice, maxPrice,
                regionId, cityId, topSubCategoryId, lowSubCategoryId, categoryId, productType, section, keyword);

        Sort sort = buildSort(sortOrder);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        return advertisementRepository.findAll(specs, pageable);
    }

    private List<FilteredAdvertisementsResponse> mapToResponse(Page<Advertisement> advertisements) {
        return advertisements.stream()
                .map(advertisementFilterMapper::mapToFilteredAdvertisementsResponse)
                .collect(Collectors.toList());
    }

    private Sort buildSort(String sortOrder) {
        Sort priceSort = sortStrategyFactory.getSortStrategy(sortOrder).getSortOrder();
        Sort dateSort = sortStrategyFactory.getSortStrategy(DATE_DESCENDING).getSortOrder();
        return priceSort.and(dateSort);
    }

    private PriceRangeDto getPriceRange(Long cityId, Long regionId,
                                        Long categoryId,
                                        Long topSubCategoryId, Long lowSubCategoryId,
                                        Advertisement.ProductType productType, Advertisement.AdSection section) {

        return advertisementRepository.findMinAndMaxPrice(cityId, regionId, categoryId,
                        topSubCategoryId, lowSubCategoryId, productType, section)
                .orElse(new PriceRangeDto(null, null));
    }

    private FilteredAdvertisementsResult buildResult(List<FilteredAdvertisementsResponse> advertisements,
                                                     long totalAdvertisements, int totalPages,
                                                     PriceRangeDto priceRange) {

        FilteredAdvertisementsResult result = new FilteredAdvertisementsResult();
        result.setAdvertisements(advertisements);
        result.setTotalPages(totalPages);
        result.setTotalAdvertisements(totalAdvertisements);
        result.setMinPrice(priceRange.getMinPrice());
        result.setMaxPrice(priceRange.getMaxPrice());
        return result;
    }
}
