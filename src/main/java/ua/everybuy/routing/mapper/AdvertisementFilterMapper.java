package ua.everybuy.routing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.AdvertisementSearchResultDto;
import ua.everybuy.routing.mapper.helper.CityMappingHelper;
import ua.everybuy.routing.mapper.helper.DescriptionHelper;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        DescriptionHelper.class,
        SubCategoryMapper.class,
        CityMappingHelper.class
})
public interface AdvertisementFilterMapper {
    @Mapping(source = "advertisement.id", target = "advertisementId")
    @Mapping(source = "advertisement.description", target = "description", qualifiedByName = "truncateDescription")
    @Mapping(source = "advertisement.topSubCategory", target = "topSubCategory", qualifiedByName = "mapToSubCategoryDto")
    @Mapping(source = "advertisement.lowSubCategory", target = "lowSubCategory", qualifiedByName = "mapToSubCategoryDto")
    @Mapping(source = "advertisement.topSubCategory.category", target = "category")
    FilteredAdvertisementsResponse mapToFilteredAdvertisementsResponse(Advertisement advertisement);

    AdvertisementSearchResultDto mapToAdvertisementPaginationDto(long totalAdvertisements, int totalPages,
                                                                 Double minPrice, Double maxPrice,
                                                                 List<FilteredAdvertisementsResponse> advertisements);
}
