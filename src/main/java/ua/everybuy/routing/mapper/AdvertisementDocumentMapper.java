package ua.everybuy.routing.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.everybuy.buisnesslogic.service.category.AdvertisementSubCategoryService;
import ua.everybuy.buisnesslogic.service.category.CategoryService;
import ua.everybuy.buisnesslogic.service.category.LowLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.buisnesslogic.service.location.CityService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AdvertisementDocumentMapper {
    private final AdvertisementSubCategoryService subCategoryService;
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
                .creationDate(convertToDate(ad.getCreationDate()))
                .updateDate(convertToDate(ad.getUpdateDate()))
                .isEnabled(ad.getIsEnabled())
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

    private Date convertToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public FilteredAdvertisementsResponse mapToFilteredAdvertisementsResponse(AdvertisementDocument doc) {
        FilteredAdvertisementsResponse response = new FilteredAdvertisementsResponse();

        response.setAdvertisementId(doc.getId());
        response.setUserId(doc.getUserId());
        response.setMainPhotoUrl(doc.getMainPhotoUrl());
        response.setTitle(doc.getTitle());
        response.setDescription(doc.getDescription());
        response.setPrice(doc.getPrice());
        response.setUpdateDate(LocalDateTime.ofInstant(doc.getUpdateDate().toInstant(), ZoneId.systemDefault()));
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
