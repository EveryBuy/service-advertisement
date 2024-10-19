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
import ua.everybuy.routing.dto.mapper.AdvertisementFilterMapper;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementFilterService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementFilterMapper advertisementFilterMapper;
    private final SortStrategyFactory sortStrategyFactory;
    private final FilterValidator filterValidator;

    public List<FilteredAdvertisementsResponse> getFilteredAdvertisements(Double minPrice, Double maxPrice,
                                                                          String sortOrder, Long regionId,
                                                                          Long topSubCategoryId, Long lowSubCategoryId,
                                                                          Long categoryId, Advertisement.ProductType productType,
                                                                          Advertisement.AdSection section,
                                                                          int page, int size) {

        Page<Advertisement> paginatedAdvertisements = applyFilters(minPrice, maxPrice, sortOrder,
                regionId, topSubCategoryId, lowSubCategoryId, categoryId, productType, section, page, size);

        return mapToResponse(paginatedAdvertisements);
    }

    public Page<Advertisement> applyFilters(Double minPrice, Double maxPrice, String sortOrder,
                                            Long regionId, Long topSubCategoryId, Long lowSubCategoryId,
                                            Long categoryId, Advertisement.ProductType productType,
                                            Advertisement.AdSection section, int page, int size) {

        filterValidator.validate(regionId, topSubCategoryId, lowSubCategoryId, categoryId);

        Specification<Advertisement> specs = Specification
                .where(AdvertisementSpecifications.hasMinPrice(minPrice))
                .and(AdvertisementSpecifications.hasMaxPrice(maxPrice))
                .and(AdvertisementSpecifications.belongsToRegion(regionId))
                .and(AdvertisementSpecifications.belongsToTopSubCategory(topSubCategoryId))
                .and(AdvertisementSpecifications.belongsToLowSubCategory(lowSubCategoryId))
                .and(AdvertisementSpecifications.belongsToCategory(categoryId))
                .and(AdvertisementSpecifications.hasProductType(productType))
                .and(AdvertisementSpecifications.hasSection(section));

        Sort sort = sortStrategyFactory.getSortStrategy(sortOrder).getSortOrder();
        Pageable pageable = PageRequest.of(page, size, sort);

        return advertisementRepository.findAll(specs, pageable);
    }

    private List<FilteredAdvertisementsResponse> mapToResponse(Page<Advertisement> advertisements) {
        return advertisements.stream()
                .map(advertisementFilterMapper::mapToFilteredAdvertisementsResponse)
                .collect(Collectors.toList());
    }
}
