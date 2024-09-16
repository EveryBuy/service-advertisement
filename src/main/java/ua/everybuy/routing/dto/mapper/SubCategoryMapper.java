package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.SubCategoryDto;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {
    @Mapping(source = "subCategory.id", target = "id")
    @Mapping(source = "subCategory.subCategoryName", target = "subCategoryName")
    @Mapping(source = "subCategory.subCategoryNameUkr", target = "subCategoryNameUkr")
    SubCategoryDto mapToSubCategoryDto(TopLevelSubCategory subCategory);
    @Mapping(source = "subCategory.id", target = "id")
    @Mapping(source = "subCategory.subCategoryName", target = "subCategoryName")
    @Mapping(source = "subCategory.subCategoryNameUkr", target = "subCategoryNameUkr")
    SubCategoryDto mapToSubCategoryDto(LowLevelSubCategory subCategory);
}
