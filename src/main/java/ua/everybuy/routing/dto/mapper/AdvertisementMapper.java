package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.ShortUserInfoDto;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;
import ua.everybuy.routing.dto.request.UpdateAdvertisementRequest;
import ua.everybuy.routing.dto.response.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = AdvertisementMappingHelper.class)
public interface AdvertisementMapper {
    @Mapping(source = "request.cityId", target = "city", qualifiedByName = "cityIdToCity")
    @Mapping(source = "topSubCategory", target = "topSubCategory")
    @Mapping(source = "lowSubCategory", target = "lowSubCategory")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "statistics", expression = "java(new AdvertisementStatistics())")
    @Mapping(target = "isEnabled", constant = "true")
    @Mapping(source = "userId", target = "userId")
    @Mapping(target = "favouriteAdvertisements", ignore = true)
    @Mapping(target = "id", ignore = true)
    Advertisement mapToEntity(CreateAdvertisementRequest request,
                              Long userId,
                              TopLevelSubCategory topSubCategory,
                              LowLevelSubCategory lowSubCategory);

    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "request.title", target = "title")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.price", target = "price")
    @Mapping(source = "request.productType", target = "productType")
    @Mapping(source = "request.section", target = "section")
    @Mapping(source = "request.cityId", target = "city", qualifiedByName = "cityIdToCity")
    @Mapping(source = "topSubCategory", target = "topSubCategory")
    @Mapping(source = "lowSubCategory", target = "lowSubCategory")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isEnabled", constant = "true")
    @Mapping(target = "favouriteAdvertisements", ignore = true)
    Advertisement mapToEntity(UpdateAdvertisementRequest request,
                              Advertisement advertisement,
                              TopLevelSubCategory topSubCategory,
                              LowLevelSubCategory lowSubCategory);

    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.city.cityName", target = "cityName")
    @Mapping(source = "advertisement.city.region.regionName", target = "regionName")
    @Mapping(source = "advertisement.topSubCategory.category", target = "category")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "photos", target = "photoUrls")
    AdvertisementDto mapToDto(Advertisement advertisement,
                              Set<String> deliveryMethods,
                              List<String> photos,
                              ShortUserInfoDto userDto);

    @Mapping(source = "advertisement.id", target = "advertisementId")
    @Mapping(source = "advertisement.isEnabled", target = "status")
    @Mapping(source = "advertisement.updateDate", target = "updateDate")
    AdvertisementStatusResponse mapToAdvertisementStatusResponse(Advertisement advertisement);

    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "advertisement.city.cityName", target = "cityName")
    @Mapping(source = "advertisement.topSubCategory.category.nameUkr", target = "categoryNameUkr")
    @Mapping(source = "advertisement.topSubCategory.subCategoryNameUkr", target = "topSubCategoryNameUkr")
    @Mapping(source = "advertisement.lowSubCategory.subCategoryNameUkr", target = "lowSubCategoryNameUkr")
    @Mapping(source = "photos", target = "photoUrls")
    CreateAdvertisementResponse mapToAdvertisementCreateResponse(Advertisement advertisement,
                                                                 Set<String> deliveryMethods,
                                                                 List<String> photos);

    @Mapping(source = "advertisement.city.cityName", target = "cityName")
    @Mapping(source = "advertisement.topSubCategory.category.nameUkr", target = "categoryNameUkr")
    @Mapping(source = "advertisement.topSubCategory.subCategoryNameUkr", target = "topSubCategoryNameUkr")
    @Mapping(source = "advertisement.lowSubCategory.subCategoryNameUkr", target = "lowSubCategoryNameUkr")
    @Mapping(source = "photos", target = "photoUrls")
    UpdateAdvertisementResponse mapToAdvertisementUpdateResponse(Advertisement advertisement,
                                                                 Set<String> deliveryMethods,
                                                                 List<String> photos);

    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "advertisement.title", target = "title")
    @Mapping(source = "advertisement.price", target = "price")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "advertisement.statistics.views", target = "views")
    @Mapping(source = "advertisement.statistics.favouriteCount", target = "favouriteCount")
    AdvertisementWithStatisticResponse mapToAdvertisementStatisticResponse(Advertisement advertisement);

    @Mapping(source = "advertisement.id", target = "advertisementId")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "advertisement.title", target = "title")
    @Mapping(source = "advertisement.productType", target = "productType")
    @Mapping(source = "advertisement.price", target = "price")
    @Mapping(source = "advertisement.description", target = "description", qualifiedByName = "truncateDescription")
    @Mapping(source = "advertisement.updateDate", target = "updateDate")
    @Mapping(source = "advertisement.city", target = "city")
    FilteredAdvertisementsResponse mapToFilteredAdvertisementsResponse(Advertisement advertisement);

    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "advertisement.title", target = "title")
    AdvertisementInfoForChatService mapToAdvertisementInfoForChatService(Advertisement advertisement);
}
