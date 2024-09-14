package ua.everybuy.routing.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.everybuy.database.entity.SubCategory;
import ua.everybuy.routing.dto.SubCategoryDto;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {
    @Mapping(source = "subCategory.id", target = "id")
    @Mapping(source = "subCategory.subCategoryName", target = "subCategoryName")
    @Mapping(source = "subCategory.subCategoryNameUkr", target = "subCategoryNameUkr")
    SubCategoryDto mapToSubCategoryDto(SubCategory subCategory);
}
