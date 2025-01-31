package ua.everybuy.routing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.SubCategoryDto;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {
    @Named("mapToSubCategoryDto")
    @Mapping(source = "subCategory.id", target = "id")
    @Mapping(source = "subCategory.subCategoryName", target = "subCategoryName")
    @Mapping(source = "subCategory.subCategoryNameUkr", target = "subCategoryNameUkr")
    SubCategoryDto mapToSubCategoryDto(TopLevelSubCategory subCategory);

    @Named("mapToSubCategoryDto")
    @Mapping(source = "subCategory.id", target = "id")
    @Mapping(source = "subCategory.subCategoryName", target = "subCategoryName")
    @Mapping(source = "subCategory.subCategoryNameUkr", target = "subCategoryNameUkr")
    SubCategoryDto mapToSubCategoryDto(LowLevelSubCategory subCategory);
}
