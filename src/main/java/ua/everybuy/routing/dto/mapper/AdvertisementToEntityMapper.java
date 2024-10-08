package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementStatistics;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;
import ua.everybuy.routing.dto.request.UpdateAdvertisementRequest;
import ua.everybuy.routing.dto.mapper.helper.*;

@Mapper(componentModel = "spring", uses = {
        CityMappingHelper.class,
        CategoryMappingHelper.class
})
public interface AdvertisementToEntityMapper {
    @Mapping(source = "request.cityId", target = "city", qualifiedByName = "cityIdToCity")
    @Mapping(source = "request", target = "topSubCategory", qualifiedByName = "getTopLevelSubCategory")
    @Mapping(source = "request", target = "lowSubCategory", qualifiedByName = "getLowLevelSubCategory")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isEnabled", constant = "true")
    @Mapping(source = "userId", target = "userId")
    @Mapping(target = "favouriteAdvertisements", ignore = true)
    @Mapping(target = "id", ignore = true)
    Advertisement mapToEntity(CreateAdvertisementRequest request,
                              AdvertisementStatistics statistics, Long userId);

    @Mapping(source = "request.cityId", target = "city", qualifiedByName = "cityIdToCity")
    @Mapping(source = "request", target = "topSubCategory", qualifiedByName = "getTopLevelSubCategory")
    @Mapping(source = "request", target = "lowSubCategory", qualifiedByName = "getLowLevelSubCategory")
    @Mapping(source = "advertisement.id", target = "id")
    @Mapping(source = "request.title", target = "title")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.price", target = "price")
    @Mapping(source = "request.productType", target = "productType")
    @Mapping(source = "request.section", target = "section")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isEnabled", constant = "true")
    @Mapping(target = "favouriteAdvertisements", ignore = true)
    Advertisement mapToEntity(UpdateAdvertisementRequest request, Advertisement advertisement);
}
