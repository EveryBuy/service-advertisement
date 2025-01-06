package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.mapper.helper.*;

@Mapper(componentModel = "spring", uses = {
        PhotoMappingHelper.class,
        UserMappingHelper.class,
        DeliveryMappingHelper.class,
        CityMappingHelper.class,
})
public interface AdvertisementToDtoMapper {
    @Mapping(source = "advertisement", target = "photoUrls", qualifiedByName = "getPhotoUrls")
    @Mapping(source = "advertisement", target = "deliveryMethods", qualifiedByName = "getDeliveryMethods")
    @Mapping(source = "advertisement", target = "userDto", qualifiedByName = "getUserInfo")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.city.id", target = "city", qualifiedByName = "cityIdToCity")
    @Mapping(source = "advertisement.topSubCategory.category", target = "category")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    AdvertisementDto mapToDto(Advertisement advertisement);
}
