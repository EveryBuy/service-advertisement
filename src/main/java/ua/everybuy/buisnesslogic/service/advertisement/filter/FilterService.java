package ua.everybuy.buisnesslogic.service.advertisement.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.advertisement.AdvertisementStorageService;
import ua.everybuy.buisnesslogic.service.category.LowLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.location.RegionService;
import ua.everybuy.buisnesslogic.service.category.CategoryService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.mapper.AdvertisementFilterMapper;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ua.everybuy.errorhandling.message.FilterAdvertisementValidationMessages.INVALID_SORT_ORDER_MESSAGE;

@Service
@RequiredArgsConstructor
public class FilterService {
    private static final String SORT_ORDER_ASC = "ASC";
    private static final String SORT_ORDER_DESC = "DESC";
    private static final Comparator<Advertisement> PRICE_ASC_COMPARATOR = Comparator
            .comparing(Advertisement::getPrice);
    private static final Comparator<Advertisement> PRICE_DESC_COMPARATOR = Comparator
            .comparing(Advertisement::getPrice).reversed();

    private final AdvertisementStorageService advertisementStorageService;
    private final AdvertisementFilterMapper advertisementFilterMapper;
    private final TopLevelSubCategoryService topLevelSubCategoryService;
    private final LowLevelSubCategoryService lowLevelSubCategoryService;
    private final CategoryService categoryService;
    private final RegionService regionService;

    public List<FilteredAdvertisementsResponse> getFilteredAdvertisements(Double minPrice, Double maxPrice,
                                                                          String sortOrder, Long regionId,
                                                                          Long topSubCategoryId, Long lowSubCategoryId,
                                                                          Long categoryId, Advertisement.ProductType productType,
                                                                          Advertisement.AdSection section,
                                                                          int page, int size) {

        List<Advertisement> filteredAdvertisements = applyFilters(minPrice, maxPrice, sortOrder,
                regionId, topSubCategoryId, lowSubCategoryId, categoryId, productType, section);

        int fromIndex = Math.min(page * size, filteredAdvertisements.size());
        int toIndex = Math.min(fromIndex + size, filteredAdvertisements.size());

        List<Advertisement> paginatedAdvertisements = filteredAdvertisements.subList(fromIndex, toIndex);

        return mapToResponse(paginatedAdvertisements);
    }

    public List<Advertisement> applyFilters(Double minPrice, Double maxPrice, String sortOrder,
                                            Long regionId, Long topSubCategoryId, Long lowSubCategoryId,
                                            Long categoryId, Advertisement.ProductType productType,
                                            Advertisement.AdSection section) {

        List<Advertisement> allAdvertisements = advertisementStorageService.getActiveAdvertisements();

        List<Advertisement> filteredAds = allAdvertisements.stream()
                .filter(ad -> filterByMinPrice(ad, minPrice))
                .filter(ad -> filterByMaxPrice(ad, maxPrice))
                .filter(ad -> filterByCity(ad, regionId))
                .filter(ad -> filterByTopSubCategory(ad, topSubCategoryId))
                .filter(ad -> filterByLowSubCategory(ad, lowSubCategoryId))
                .filter(ad -> filterByCategory(ad, categoryId))
                .filter(ad -> filterByProductType(ad, productType))
                .filter(ad -> filterBySection(ad, section))
                .collect(Collectors.toList());

        if (sortOrder != null && !sortOrder.isBlank()) {
            filteredAds.sort(getPriceComparator(sortOrder));
        }
        return filteredAds;
    }

    private boolean filterByMinPrice(Advertisement ad, Double minPrice) {
        return minPrice == null || ad.getPrice() >= minPrice;
    }

    private boolean filterByMaxPrice(Advertisement ad, Double maxPrice) {
        return maxPrice == null || ad.getPrice() <= maxPrice;
    }

    private boolean filterByCity(Advertisement ad, Long regionId) {
        if (regionId != null) {
            regionService.findById(regionId);
        }
        return regionId == null || ad.getCity().getRegion().getId().equals(regionId);
    }

    private boolean filterByTopSubCategory(Advertisement ad, Long topSubCategoryId) {
        if (topSubCategoryId != null) {
            topLevelSubCategoryService.findById(topSubCategoryId);
        }
        return topSubCategoryId == null || ad.getTopSubCategory().getId().equals(topSubCategoryId);
    }

    private boolean filterByLowSubCategory(Advertisement ad, Long lowSubCategoryId) {
        if (lowSubCategoryId != null) {
            lowLevelSubCategoryService.findById(lowSubCategoryId);
        }
        return lowSubCategoryId == null || ad.getLowSubCategory().getId().equals(lowSubCategoryId);
    }

    private boolean filterByCategory(Advertisement ad, Long categoryId) {
        if (categoryId != null) {
            categoryService.findById(categoryId);
        }
        return categoryId == null || ad.getTopSubCategory().getCategory().getId().equals(categoryId);
    }

    private boolean filterByProductType(Advertisement ad, Advertisement.ProductType productType) {
        return productType == null || ad.getProductType() == productType;
    }

    private boolean filterBySection(Advertisement ad, Advertisement.AdSection adSection) {
        return adSection == null || ad.getSection() == adSection;
    }

    private Comparator<Advertisement> getPriceComparator(String sortOrder) {
        if (SORT_ORDER_ASC.equalsIgnoreCase(sortOrder)) {
            return PRICE_ASC_COMPARATOR;
        } else if (SORT_ORDER_DESC.equalsIgnoreCase(sortOrder)) {
            return PRICE_DESC_COMPARATOR;
        } else {
            throw new IllegalArgumentException(INVALID_SORT_ORDER_MESSAGE);
        }
    }

    private List<FilteredAdvertisementsResponse> mapToResponse(List<Advertisement> advertisements) {
        return advertisements.stream()
                .map(advertisementFilterMapper::mapToFilteredAdvertisementsResponse)
                .collect(Collectors.toList());
    }
}
