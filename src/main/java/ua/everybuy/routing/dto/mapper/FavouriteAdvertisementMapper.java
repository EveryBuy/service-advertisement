package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.FavouriteAdvertisement;
import ua.everybuy.routing.dto.response.FavouriteAdvertisementResponse;
import ua.everybuy.routing.dto.response.ShortAdvertisementResponse;

@Mapper
public interface FavouriteAdvertisementMapper {
    FavouriteAdvertisementMapper INSTANCE = Mappers.getMapper(FavouriteAdvertisementMapper.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "advertisement.id", target = "advertisementId")
    FavouriteAdvertisementResponse toFavouriteAdvertisementResponse(FavouriteAdvertisement favouriteAdvertisement);

    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "advertisement.title", target = "title")
    @Mapping(source = "advertisement.price", target = "price")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    ShortAdvertisementResponse toShortAdvertisementResponse(Advertisement advertisement);
}
