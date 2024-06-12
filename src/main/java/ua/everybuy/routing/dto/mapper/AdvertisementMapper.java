package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;
import ua.everybuy.routing.dto.response.CreateAdvertisementResponse;
import ua.everybuy.routing.dto.response.FavouriteAdvertisementResponse;
import ua.everybuy.routing.dto.response.ShortAdvertisementResponse;

import java.util.List;

@Mapper(componentModel = "spring", uses = AdvertisementMappingHelper.class)
public interface AdvertisementMapper {

    @Mapping(source = "request.cityId", target = "city", qualifiedByName = "cityIdToCity")
    @Mapping(source = "request.subCategoryId", target = "subCategory", qualifiedByName = "subCategoryIdToSubCategory")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isEnabled", constant = "true")
    Advertisement mapToEntity(CreateAdvertisementRequest request);

    @Mapping(source = "advertisement.city", target = "city")
    @Mapping(source = "advertisement.subCategory", target = "subCategory")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "photos", target = "photoUrls")
    AdvertisementDto mapToDto(Advertisement advertisement, List<String> photos);

    @Mapping(source = "advertisement.city.cityName", target = "cityName")
    @Mapping(source = "advertisement.subCategory.subCategoryName", target = "subCategoryName")
    @Mapping(source = "photos", target = "photoUrls")
    CreateAdvertisementResponse mapToCreateAdvertisementResponse(Advertisement advertisement, List<String> photos);


    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "advertisement.title", target = "title")
    @Mapping(source = "advertisement.price", target = "price")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    ShortAdvertisementResponse mapToShortAdvertisementResponse(Advertisement advertisement);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "advertisement.id", target = "advertisementId")
    FavouriteAdvertisementResponse mapToFavouriteAdvertisementResponse(FavouriteAdvertisement favouriteAdvertisement);

}
