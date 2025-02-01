package ua.everybuy.routing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.mapper.helper.CityMappingHelper;
import ua.everybuy.routing.mapper.helper.DescriptionHelper;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;

@Mapper(componentModel = "spring", uses = {
        DescriptionHelper.class,
        SubCategoryMapper.class,
        CityMappingHelper.class
})
public interface AdvertisementFilterMapper {
    @Mapping(source = "advertisement.id", target = "advertisementId")
    @Mapping(source = "advertisement.userId", target = "userId")
    @Mapping(source = "advertisement.mainPhotoUrl", target = "mainPhotoUrl")
    @Mapping(source = "advertisement.title", target = "title")
    @Mapping(source = "advertisement.productType", target = "productType")
    @Mapping(source = "advertisement.price", target = "price")
    @Mapping(source = "advertisement.description", target = "description", qualifiedByName = "truncateDescription")
    @Mapping(source = "advertisement.updateDate", target = "updateDate")
    @Mapping(source = "advertisement.city", target = "city")
    @Mapping(source = "advertisement.topSubCategory", target = "topSubCategory", qualifiedByName = "mapToSubCategoryDto")
    @Mapping(source = "advertisement.lowSubCategory", target = "lowSubCategory", qualifiedByName = "mapToSubCategoryDto")
    @Mapping(source = "advertisement.topSubCategory.category", target = "category")
    FilteredAdvertisementsResponse mapToFilteredAdvertisementsResponse(Advertisement advertisement);
}
