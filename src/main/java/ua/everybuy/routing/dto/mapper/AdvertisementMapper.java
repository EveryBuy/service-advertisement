package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;
import ua.everybuy.routing.dto.request.UpdateAdvertisementRequest;
import ua.everybuy.routing.dto.response.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = AdvertisementMappingHelper.class)
public interface AdvertisementMapper {

    @Mapping(source = "request.cityId", target = "city", qualifiedByName = "cityIdToCity")
    @Mapping(source = "request.subCategoryId", target = "subCategory", qualifiedByName = "subCategoryIdToSubCategory")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isEnabled", constant = "true")
    @Mapping(target = "views", constant = "0")
    @Mapping(target = "favouriteCount", constant = "0")
    @Mapping(source = "userId", target = "userId")
    @Mapping(target = "favouriteAdvertisements", ignore = true)
    Advertisement mapToEntity(CreateAdvertisementRequest request, Long userId);

    @Mapping(source = "request.title", target = "title")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.price", target = "price")
    @Mapping(source = "request.productType", target = "productType")
    @Mapping(source = "request.deliveryMethods", target = "deliveryMethods")
    @Mapping(source = "request.cityId", target = "city", qualifiedByName = "cityIdToCity")
    @Mapping(source = "request.subCategoryId", target = "subCategory", qualifiedByName = "subCategoryIdToSubCategory")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isEnabled", constant = "true")
    @Mapping(target = "favouriteAdvertisements", ignore = true)
    Advertisement mapToEntity(UpdateAdvertisementRequest request, Advertisement advertisement);

    @Mapping(source = "advertisement.city", target = "city")
    @Mapping(source = "advertisement.subCategory", target = "subCategory")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "photos", target = "photoUrls")
    AdvertisementDto mapToDto(Advertisement advertisement, List<String> photos);

    @Mapping(source = "advertisement.id", target = "advertisementId")
    @Mapping(source = "advertisement.isEnabled", target = "status")
    @Mapping(source = "advertisement.updateDate", target = "updateDate")
    AdvertisementStatusResponse mapToAdvertisementStatusResponse(Advertisement advertisement);

    @Mapping(source = "advertisement.city.cityName", target = "cityName")
    @Mapping(source = "advertisement.subCategory.subCategoryName", target = "subCategoryName")
    @Mapping(source = "photos", target = "photoUrls")
    CreateAdvertisementResponse mapToAdvertisementCreateResponse(Advertisement advertisement, List<String> photos);

    @Mapping(source = "advertisement.city.cityName", target = "cityName")
    @Mapping(source = "advertisement.subCategory.subCategoryName", target = "subCategoryName")
    @Mapping(source = "photos", target = "photoUrls")
    UpdateAdvertisementResponse mapToAdvertisementUpdateResponse(Advertisement advertisement, List<String> photos);

    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "advertisement.title", target = "title")
    @Mapping(source = "advertisement.price", target = "price")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    AdvertisementWithStatisticResponse mapToShortAdvertisementResponse(Advertisement advertisement);

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
}

