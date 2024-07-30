package ua.everybuy.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.mapper.AdvertisementMapper;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilterService {
    private static final String SORT_ORDER_ASC = "ASC";
    private static final String SORT_ORDER_DESC = "DESC";
    private static final Comparator<Advertisement> PRICE_ASC_COMPARATOR = Comparator.comparing(Advertisement::getPrice);
    private static final Comparator<Advertisement> PRICE_DESC_COMPARATOR = Comparator.comparing(Advertisement::getPrice).reversed();

    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final RegionService regionService;

    public List<FilteredAdvertisementsResponse> getFilteredAdvertisements(Double minPrice, Double maxPrice, String sortOrder,
                                                                          Long regionId, Long subCategoryId,
                                                                          Long categoryId, String productType) {
        List<Advertisement> filteredAdvertisements = filterAdvertisements(minPrice, maxPrice, sortOrder, regionId, subCategoryId, categoryId, productType);
        return mapToResponse(filteredAdvertisements);
    }

    private List<Advertisement> filterAdvertisements(Double minPrice, Double maxPrice, String sortOrder,
                                                     Long cityId, Long subCategoryId,
                                                     Long categoryId, String productType) {

        List<Advertisement> advertisements = advertisementService.findAllEnabledAdsOrderByCreationDateDesc();

        advertisements = filterByMinPrice(advertisements, minPrice);
        advertisements = filterByMaxPrice(advertisements, maxPrice);
        advertisements = sortAdvertisements(advertisements, sortOrder);
        advertisements = filterByCity(advertisements, cityId);
        advertisements = filterBySubCategory(advertisements, subCategoryId);
        advertisements = filterByCategory(advertisements, categoryId);
        advertisements = filterByProductType(advertisements, productType);

        return advertisements;
    }

    private List<Advertisement> filterByMinPrice(List<Advertisement> advertisements, Double minPrice) {
        return Optional.ofNullable(minPrice)
                .filter(price -> price > 0)
                .map(price -> advertisements.stream()
                        .filter(ad -> ad.getPrice() >= price)
                        .collect(Collectors.toList()))
                .orElse(advertisements);
    }

    private List<Advertisement> filterByMaxPrice(List<Advertisement> advertisements, Double maxPrice) {
        return Optional.ofNullable(maxPrice)
                .filter(price -> price > 0)
                .map(price -> advertisements.stream()
                        .filter(ad -> ad.getPrice() <= price)
                        .collect(Collectors.toList()))
                .orElse(advertisements);
    }

    private List<Advertisement> sortAdvertisements(List<Advertisement> advertisements, String sortOrder) {
        if (sortOrder != null && !sortOrder.isBlank()) {
            return switch (sortOrder.toUpperCase()) {
                case SORT_ORDER_ASC -> advertisements.stream().sorted(PRICE_ASC_COMPARATOR).collect(Collectors.toList());
                case SORT_ORDER_DESC -> advertisements.stream().sorted(PRICE_DESC_COMPARATOR).collect(Collectors.toList());
                default -> advertisements;
            };
        }
        return advertisements;
    }

    private List<Advertisement> filterByCity(List<Advertisement> advertisements, Long regionId) {
        return Optional.ofNullable(regionId)
                .map(id -> {
                    regionService.findById(id);
                    return advertisements.stream()
                            .filter(ad -> ad.getCity().getRegion().getId().equals(id))
                            .collect(Collectors.toList());
                })
                .orElse(advertisements);
    }

    private List<Advertisement> filterBySubCategory(List<Advertisement> advertisements, Long subCategoryId) {
        return Optional.ofNullable(subCategoryId)
                .map(id -> {
                    subCategoryService.findById(id);
                    return advertisements.stream()
                            .filter(ad -> ad.getSubCategory().getId().equals(id))
                            .collect(Collectors.toList());
                })
                .orElse(advertisements);
    }

    private List<Advertisement> filterByCategory(List<Advertisement> advertisements, Long categoryId) {
        return Optional.ofNullable(categoryId)
                .map(id -> {
                    categoryService.findById(id);
                    return advertisements.stream()
                            .filter(ad -> ad.getSubCategory().getCategory().getId().equals(id))
                            .collect(Collectors.toList());
                })
                .orElse(advertisements);
    }

    private List<Advertisement> filterByProductType(List<Advertisement> advertisements, String productType) {
        return Optional.ofNullable(productType)
                .filter(type -> !type.isBlank())
                .map(type -> {
                    String typeOfProd = type.toUpperCase();
                    return advertisements.stream()
                            .filter(ad -> ad.getProductType().equals(Advertisement.ProductType.valueOf(typeOfProd)))
                            .collect(Collectors.toList());
                })
                .orElse(advertisements);
    }

    private List<FilteredAdvertisementsResponse> mapToResponse(List<Advertisement> advertisements) {
        return advertisements.stream()
                .map(advertisementMapper::mapToFilteredAdvertisementsResponse)
                .collect(Collectors.toList());
    }
}
