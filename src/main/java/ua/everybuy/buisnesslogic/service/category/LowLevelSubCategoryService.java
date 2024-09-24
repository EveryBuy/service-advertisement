package ua.everybuy.buisnesslogic.service.category;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.repository.LowLevelSubCategoryRepository;
import ua.everybuy.routing.dto.SubCategoryDto;
import ua.everybuy.routing.dto.mapper.SubCategoryMapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LowLevelSubCategoryService {
    private final LowLevelSubCategoryRepository lowLevelSubCategoryRepository;
    private final TopLevelSubCategoryService topLevelSubCategoryService;
    private final SubCategoryMapper mapper;

    public LowLevelSubCategory findById(Long id) {
        return lowLevelSubCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Low-level subcategory not found"));
    }

    public List<LowLevelSubCategory> findByTopLevelSubCategoryId(Long topLevelSubCategoryId) {
        topLevelSubCategoryService.findById(topLevelSubCategoryId);
        return lowLevelSubCategoryRepository.findByTopLevelSubCategoryId(topLevelSubCategoryId);
    }

    public List<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId) {
        List<LowLevelSubCategory> subCategories = findByTopLevelSubCategoryId(categoryId);
        return subCategories.stream()
                .map(mapper::mapToSubCategoryDto)
                .collect(Collectors.toList());
    }

    public LowLevelSubCategory findLowLevelSubCategoryByTopLevelId(Long topLevelSubCategoryId,
                                                                   Long lowLevelSubCategoryId) {
        topLevelSubCategoryService.findById(topLevelSubCategoryId);
        LowLevelSubCategory lowLevelSubCategory = findById(lowLevelSubCategoryId);

        if (!lowLevelSubCategory.getTopLevelSubCategory()
                .getId().equals(topLevelSubCategoryId)) {
            throw new IllegalArgumentException(
                    "Low-level subcategory does not belong to the specified top-level subcategory");
        }
        return lowLevelSubCategory;
    }
}
