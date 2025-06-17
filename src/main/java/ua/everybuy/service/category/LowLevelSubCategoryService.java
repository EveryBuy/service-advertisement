package ua.everybuy.service.category;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.repository.category.LowLevelSubCategoryRepository;
import ua.everybuy.routing.dto.SubCategoryDto;
import ua.everybuy.routing.mapper.SubCategoryMapper;
import java.util.List;
import java.util.stream.Collectors;
import static ua.everybuy.errorhandling.message.CategoryValidationMessages.LOW_LEVEL_SUBCATEGORY_ERROR_MESSAGE;
import static ua.everybuy.errorhandling.message.CategoryValidationMessages.LOW_LEVEL_SUBCATEGORY_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class LowLevelSubCategoryService {
    private final LowLevelSubCategoryRepository lowLevelSubCategoryRepository;
    private final TopLevelSubCategoryService topLevelSubCategoryService;
    private final SubCategoryMapper mapper;

    @Cacheable(value = "lowLevelSubCategoryCache", key = "#id")
    public LowLevelSubCategory findById(Long id) {
        return lowLevelSubCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        LOW_LEVEL_SUBCATEGORY_NOT_FOUND_MESSAGE+id));
    }

    public List<LowLevelSubCategory> findByTopLevelSubCategoryId(Long topLevelSubCategoryId) {
        topLevelSubCategoryService.findById(topLevelSubCategoryId);
        return lowLevelSubCategoryRepository.findByTopLevelSubCategoryId(topLevelSubCategoryId);
    }

    @Cacheable(value = "subCategoriesByCategoryCache", key = "#topLevelSubCategoryId")
    public List<SubCategoryDto> getSubCategoriesByCategoryId(Long topLevelSubCategoryId) {
        List<LowLevelSubCategory> subCategories = findByTopLevelSubCategoryId(topLevelSubCategoryId);
        return subCategories.stream()
                .map(mapper::mapToSubCategoryDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "lowLevelSubCategoryByTopLevelCache", key = "#topLevelSubCategoryId + '_' + #lowLevelSubCategoryId")
    public LowLevelSubCategory findLowLevelSubCategoryByTopLevelId(Long topLevelSubCategoryId,
                                                                   Long lowLevelSubCategoryId) {
        topLevelSubCategoryService.findById(topLevelSubCategoryId);
        LowLevelSubCategory lowLevelSubCategory = findById(lowLevelSubCategoryId);

        if (!lowLevelSubCategory.getTopLevelSubCategory()
                .getId().equals(topLevelSubCategoryId)) {
            throw new IllegalArgumentException(
                    LOW_LEVEL_SUBCATEGORY_ERROR_MESSAGE);
        }
        return lowLevelSubCategory;
    }
}
