package ua.everybuy.buisnesslogic.service.category;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.database.repository.TopLevelSubCategoryRepository;
import ua.everybuy.routing.dto.SubCategoryDto;
import ua.everybuy.routing.dto.mapper.SubCategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopLevelSubCategoryService {
    private final TopLevelSubCategoryRepository topLevelSubCategoryRepository;
    private final SubCategoryMapper mapper;
    private final CategoryService categoryService;

    public TopLevelSubCategory findById(Long id) {
        return topLevelSubCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Top-level subcategory not found"));
    }

    public List<TopLevelSubCategory> findTopLevelSubCategoriesByCategoryId(Long categoryId) {
        categoryService.findById(categoryId);
        return topLevelSubCategoryRepository.findByCategoryId(categoryId);
    }

    public List<SubCategoryDto> getSubCategoriesByTopSubcategoryId(Long subcategoryId) {
        List<TopLevelSubCategory> subCategories = findTopLevelSubCategoriesByCategoryId(subcategoryId);
        return subCategories.stream()
                .map(mapper::mapToSubCategoryDto)
                .collect(Collectors.toList());
    }

    public TopLevelSubCategory getTopLevelSubCategoryByCategoryAndSubCategoryId(Long categoryId, Long subCategoryId) {
        categoryService.findById(categoryId);
        TopLevelSubCategory topSubCategory = findById(subCategoryId);

        if (!topSubCategory.getCategory().getId().equals(categoryId)) {
            throw new EntityNotFoundException("Subcategory does not belong to the specified category.");
        }
        return topSubCategory;
    }
}

