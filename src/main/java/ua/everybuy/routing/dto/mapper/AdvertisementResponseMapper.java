package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.response.*;
import ua.everybuy.routing.dto.mapper.helper.*;

@Mapper(componentModel = "spring", uses = {
        PhotoMappingHelper.class,
        DeliveryMappingHelper.class,
})
public interface AdvertisementResponseMapper {
    @Mapping(source = "advertisement.id", target = "advertisementId")
    @Mapping(source = "advertisement.isEnabled", target = "status")
    @Mapping(source = "advertisement.updateDate", target = "updateDate")
    AdvertisementStatusResponse mapToAdvertisementStatusResponse(Advertisement advertisement);

    @Mapping(source = "advertisement", target = "photoUrls", qualifiedByName = "getPhotoUrls")
    @Mapping(source = "advertisement", target = "deliveryMethods", qualifiedByName = "getDeliveryMethods")
    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "advertisement.city.cityName", target = "cityName")
    @Mapping(source = "advertisement.topSubCategory.category.nameUkr", target = "categoryNameUkr")
    @Mapping(source = "advertisement.topSubCategory.subCategoryNameUkr", target = "topSubCategoryNameUkr")
    @Mapping(source = "advertisement.lowSubCategory.subCategoryNameUkr", target = "lowSubCategoryNameUkr")
    CreateAdvertisementResponse mapToAdvertisementCreateResponse(Advertisement advertisement);

    @Mapping(source = "advertisement", target = "photoUrls", qualifiedByName = "getPhotoUrls")
    @Mapping(source = "advertisement", target = "deliveryMethods", qualifiedByName = "getDeliveryMethods")
    @Mapping(source = "advertisement.city.cityName", target = "cityName")
    @Mapping(source = "advertisement.topSubCategory.category.nameUkr", target = "categoryNameUkr")
    @Mapping(source = "advertisement.topSubCategory.subCategoryNameUkr", target = "topSubCategoryNameUkr")
    @Mapping(source = "advertisement.lowSubCategory.subCategoryNameUkr", target = "lowSubCategoryNameUkr")
    UpdateAdvertisementResponse mapToAdvertisementUpdateResponse(Advertisement advertisement);

    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "advertisement.title", target = "title")
    @Mapping(source = "advertisement.price", target = "price")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "advertisement.statistics.views", target = "views")
    @Mapping(source = "advertisement.statistics.favouriteCount", target = "favouriteCount")
    AdvertisementWithStatisticResponse mapToAdvertisementStatisticResponse(Advertisement advertisement);

    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "advertisement.title", target = "title")
    AdvertisementInfoForChatService mapToAdvertisementInfoForChatService(Advertisement advertisement);
}
