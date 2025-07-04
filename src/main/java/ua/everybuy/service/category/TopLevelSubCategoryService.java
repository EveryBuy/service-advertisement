package ua.everybuy.service.category;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.database.repository.category.TopLevelSubCategoryRepository;
import ua.everybuy.routing.dto.SubCategoryDto;
import ua.everybuy.routing.dto.TopCategorySearchResultDto;
import ua.everybuy.routing.mapper.SubCategoryMapper;
import java.util.List;
import java.util.stream.Collectors;
import static ua.everybuy.errorhandling.message.CategoryValidationMessages.TOP_LEVEL_SUBCATEGORY_INVALID_CATEGORY_MESSAGE;
import static ua.everybuy.errorhandling.message.CategoryValidationMessages.TOP_LEVEL_SUBCATEGORY_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class TopLevelSubCategoryService {
    private final TopLevelSubCategoryRepository topLevelSubCategoryRepository;
    private final SubCategoryMapper mapper;
    private final CategoryService categoryService;

    @Cacheable(value = "topLevelSubCategoryCache", key = "#id")
    public TopLevelSubCategory findById(Long id) {
        return topLevelSubCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TOP_LEVEL_SUBCATEGORY_NOT_FOUND_MESSAGE));
    }

    public List<TopLevelSubCategory> findTopLevelSubCategoriesByCategoryId(Long categoryId) {
        categoryService.findById(categoryId);
        return topLevelSubCategoryRepository.findByCategoryId(categoryId);
    }

    @Cacheable(value = "subCategoriesByTopSubcategoryCache", key = "#subcategoryId")
    public List<SubCategoryDto> getSubCategoriesByTopSubcategoryId(Long subcategoryId) {
        List<TopLevelSubCategory> subCategories = findTopLevelSubCategoriesByCategoryId(subcategoryId);
        return subCategories.stream()
                .map(mapper::mapToSubCategoryDto)
                .collect(Collectors.toList());
    }

    public TopLevelSubCategory findTopLevelSubCategoryByCategoryAndSubCategoryId(Long categoryId,
                                                                                 Long subCategoryId) {
        categoryService.findById(categoryId);
        TopLevelSubCategory topSubCategory = findById(subCategoryId);

        if (!topSubCategory.getCategory().getId().equals(categoryId)) {
            throw new IllegalArgumentException(TOP_LEVEL_SUBCATEGORY_INVALID_CATEGORY_MESSAGE);
        }
        return topSubCategory;
    }

    public List<TopLevelSubCategory> getTopCategoriesById(List<Long> categoryIds) {
        return topLevelSubCategoryRepository.findByIdIn(categoryIds);
    }

    @Cacheable(value = "topCategoriesDtoCache", key = "#ids")
    public List<TopCategorySearchResultDto> getSearchResultDto(List<Long> ids) {
        List<TopLevelSubCategory> subCategories = getTopCategoriesById(ids);

        return subCategories.stream()
                .map(mapper::mapToTopCategoryUniqueDto)
                .collect(Collectors.toList());

    }
}
