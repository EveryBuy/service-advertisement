package ua.everybuy.routing.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.everybuy.service.category.CategoryService;
import ua.everybuy.service.category.LowLevelSubCategoryService;
import ua.everybuy.service.category.TopLevelSubCategoryService;
import ua.everybuy.service.location.CityService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;

@Component
@RequiredArgsConstructor
public class AdvertisementDocumentMapper {
    private final CategoryService categoryService;
    private final CityService cityService;
    private final TopLevelSubCategoryService topLevelSubCategoryService;
    private final LowLevelSubCategoryService lowLevelSubCategoryService;
    private final SubCategoryMapper subCategoryMapper;

    public AdvertisementDocument mapToDocument(Advertisement ad) {
        return AdvertisementDocument.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .price(ad.getPrice())
                .creationDate(String.valueOf(ad.getCreationDate()))
                .updateDate(String.valueOf(ad.getUpdateDate()))
                .isEnabled(ad.getIsEnabled())
                .isNegotiable(ad.getIsNegotiable())
                .userId(ad.getUserId())
                .mainPhotoUrl(ad.getMainPhotoUrl())
                .cityId(ad.getCity().getId())
                .categoryId(ad.getTopSubCategory().getCategory().getId())
                .topSubCategoryId(ad.getTopSubCategory().getId())
                .lowSubCategoryId(ad.getLowSubCategory() != null
                        ? ad.getLowSubCategory().getId() : null)
                .productType(ad.getProductType().name())
                .section(ad.getSection().name())
                .build();
    }

    public FilteredAdvertisementsResponse mapToFilteredAdvertisementsResponse(AdvertisementDocument doc) {
        FilteredAdvertisementsResponse response = new FilteredAdvertisementsResponse();

        response.setAdvertisementId(doc.getId());
        response.setUserId(doc.getUserId());
        response.setMainPhotoUrl(doc.getMainPhotoUrl());
        response.setTitle(doc.getTitle());
        response.setDescription(doc.getDescription());
        response.setPrice(doc.getPrice());
        response.setIsNegotiable(doc.getIsNegotiable());
        response.setUpdateDate(String.valueOf(doc.getUpdateDate()));
        response.setProductType(Advertisement.ProductType.valueOf(doc.getProductType()));
        response.setSection(doc.getSection());
        response.setCity(cityService.findById(doc.getCityId()));
        TopLevelSubCategory top = topLevelSubCategoryService.findById(doc.getTopSubCategoryId());
        response.setTopSubCategory(subCategoryMapper.mapToSubCategoryDto(top));

        if (doc.getLowSubCategoryId() != null) {
            LowLevelSubCategory low = lowLevelSubCategoryService.findById(doc.getLowSubCategoryId());
            response.setLowSubCategory(subCategoryMapper.mapToSubCategoryDto(low));
        }

         Category category = categoryService.findById(doc.getCategoryId());
            response.setCategory(subCategoryMapper.mapToCategoryDto(category));

        return response;
    }
}
