package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.AdvertisementDto;
import ua.everybuy.routing.dto.request.CreateAdvertisementRequest;

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


}
