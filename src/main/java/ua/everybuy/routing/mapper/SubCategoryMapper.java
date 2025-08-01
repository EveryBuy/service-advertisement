package ua.everybuy.routing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.CategoryAdvertisementCount;
import ua.everybuy.routing.dto.CategoryAdvertisementCountDto;
import ua.everybuy.routing.dto.CategoryDto;
import ua.everybuy.routing.dto.SubCategoryDto;
import ua.everybuy.routing.dto.TopCategorySearchResultDto;

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

    @Mapping(source = "categoryAdvertisementCount.category.id", target = "categoryId")
    @Mapping(source = "categoryAdvertisementCount.category.nameUkr", target = "nameUkr")
    CategoryAdvertisementCountDto mapToCategoryAdvertisementCountDto(
            CategoryAdvertisementCount categoryAdvertisementCount);

    @Named("mapToCategoryDto")
    CategoryDto mapToCategoryDto(Category category);

    @Mapping(source = "topLevelSubCategory.category.id", target = "categoryId")
    @Mapping(source = "topLevelSubCategory.category.nameUkr", target = "categoryName")
    @Mapping(source = "topLevelSubCategory.id", target = "topCategoryId")
    @Mapping(source = "topLevelSubCategory.subCategoryNameUkr", target = "topCategoryName")
    TopCategorySearchResultDto mapToTopCategoryUniqueDto(TopLevelSubCategory topLevelSubCategory);
}
