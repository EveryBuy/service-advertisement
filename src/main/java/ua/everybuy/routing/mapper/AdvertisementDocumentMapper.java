package ua.everybuy.routing.mapper;

import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class AdvertisementDocumentMapper {

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

        if (doc.getUpdateDate() != null) {
            response.setUpdateDate(LocalDateTime.ofInstant(doc.getUpdateDate().toInstant(), ZoneId.systemDefault()));
        }

        if (doc.getProductType() != null) {
            response.setProductType(Advertisement.ProductType.valueOf(doc.getProductType()));
        }
        response.setSection(doc.getSection());

        // Поля, требующие дополнительных сервисов/репозиториев (city, topSubCategory и т.д.)
        response.setCity(null);
        response.setTopSubCategory(null);
        response.setLowSubCategory(null);
        response.setCategory(null);

        return response;
    }
}
