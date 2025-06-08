package ua.everybuy.routing.mapper;

import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
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
}
