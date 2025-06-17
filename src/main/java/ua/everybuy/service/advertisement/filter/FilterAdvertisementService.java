package ua.everybuy.service.advertisement.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.everybuy.service.advertisement.filter.sort.SortStrategyFactory;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.database.repository.advertisement.spec.factory.AdvertisementDistinctTopCategorySpecificationFactory;
import ua.everybuy.database.repository.advertisement.spec.factory.AdvertisementSearchSpecificationFactory;
import ua.everybuy.routing.dto.PriceRangeDto;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;
import ua.everybuy.routing.dto.AdvertisementSearchResultDto;
import ua.everybuy.routing.mapper.AdvertisementFilterMapper;
import ua.everybuy.routing.mapper.SubCategoryMapper;
import java.util.List;
import java.util.stream.Collectors;

import static ua.everybuy.service.advertisement.filter.sort.SortStrategyFactory.DATE_DESCENDING;

@Service
@RequiredArgsConstructor
public class FilterAdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementFilterMapper advertisementFilterMapper;
    private final SubCategoryMapper subCategoryMapper;
    private final AdvertisementSearchSpecificationFactory filterAdSpecFactory;
    private final AdvertisementDistinctTopCategorySpecificationFactory distinctTopCategorySpecFactory;
    private final SortStrategyFactory sortStrategyFactory;
    private final FilterValidator filterValidator;
    private final FilterPriceRangeService filterPriceRangeService;

    public AdvertisementSearchResultDto getFilteredAdvertisements(AdvertisementSearchParametersDto searchParametersDto,
                                                                  int page, int size) {

        Page<Advertisement> filteredAds = applyFilters(searchParametersDto, page, size);

        long totalAdvertisements = filteredAds.getTotalElements();
        int totalPages = filteredAds.getTotalPages();

        PriceRangeDto priceRange = filterPriceRangeService.getPriceRange(searchParametersDto);
        List<FilteredAdvertisementsResponse> advertisements = mapToResponse(filteredAds);

        return advertisementFilterMapper.mapToAdvertisementPaginationDto(totalAdvertisements, totalPages,
                priceRange.getMinPrice(), priceRange.getMaxPrice(), advertisements);

    }

    private Page<Advertisement> applyFilters(AdvertisementSearchParametersDto searchParametersDto,
                                             int page, int size) {

        filterValidator.validate(searchParametersDto);

        Specification<Advertisement> specs = filterAdSpecFactory.createSpecification(searchParametersDto);
        Sort sort = buildSort(searchParametersDto.getSortOrder());
        Pageable pageable = PageRequest.of(page - 1,
                size, sort);
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

}
