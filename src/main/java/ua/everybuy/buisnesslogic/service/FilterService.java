package ua.everybuy.buisnesslogic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterService {

    private List<Advertisement> advertisementList;
    private final AdvertisementService advertisementService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final CityService cityService;

    public List<Advertisement> doFilter(Double price, Long cityId, Long subCategoryId,
                                        Long categoryId, String productType) {
        advertisementList = advertisementService.findAll();
        filterPrice(price);
        filterCity(cityId);
        filterSubcategory(subCategoryId);
        filterCategory(categoryId);
        filterProductType(productType);
        return advertisementList;
    }

    private void filterPrice(Double price) {
        if (price != null && price > 0) {
            advertisementList = advertisementList.stream().filter(ad -> ad.getPrice() <= price).toList();
        }
    }

    private void filterCity(Long cityId) {
        if (cityId != null) {
            cityService.findById(cityId);
            advertisementList = advertisementList.stream().filter(ad -> ad.getCity().getId().equals(cityId)).toList();
        }
    }

    private void filterSubcategory(Long subCategoryId) {
        if (subCategoryId != null) {
            subCategoryService.findById(subCategoryId);
            advertisementList = advertisementList.stream()
                    .filter(ad -> ad.getSubCategory().getId().equals(subCategoryId)).toList();
        }
    }

    private void filterCategory(Long categoryId) {
        if (categoryId != null) {
            categoryService.findById(categoryId);
            advertisementList = advertisementList.stream()
                    .filter(ad -> ad.getSubCategory().getCategory().getId().equals(categoryId)).toList();
        }
    }

    private void filterProductType(String productType) {
        if (productType != null && !productType.isBlank()) {
            String typeOfProd = productType.toUpperCase();
            advertisementList = advertisementList.stream()
                    .filter(ad -> ad.getProductType().equals(Advertisement.ProductType.valueOf(typeOfProd))).toList();
        }
    }
}
