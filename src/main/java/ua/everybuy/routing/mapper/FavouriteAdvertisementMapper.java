package ua.everybuy.routing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.routing.mapper.helper.CityMappingHelper;
import ua.everybuy.routing.dto.response.AddToFavouriteResponse;
import ua.everybuy.routing.dto.response.FavouriteAdvertisementResponse;

@Mapper(componentModel = "spring", uses = {
        CityMappingHelper.class,
})
public interface FavouriteAdvertisementMapper {
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "advertisement", target = "advertisement")
    FavouriteAdvertisement mapToFavouriteAdvertisementEntity(Long userId, Advertisement advertisement);

    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.id", target = "advertisementId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "advertisement.title", target = "title")
    @Mapping(source = "advertisement.price", target = "price")
    @Mapping(source = "advertisement.productType", target = "productType")
    @Mapping(source = "advertisement.updateDate", target = "updateDate")
    @Mapping(source = "advertisement.topSubCategory.category", target = "category")
    @Mapping(source = "advertisement.city.id", target = "city", qualifiedByName = "cityIdToCity")
    FavouriteAdvertisementResponse mapToFavouriteAdvertisementResponse(Advertisement advertisement);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "advertisement.id", target = "advertisementId")
    AddToFavouriteResponse mapToAddToFavouriteResponse(FavouriteAdvertisement favouriteAdvertisement);
}
