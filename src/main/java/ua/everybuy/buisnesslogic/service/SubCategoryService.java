package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.SubCategory;
import ua.everybuy.database.repository.SubCategoryRepository;
import ua.everybuy.routing.dto.SubCategoryDto;
import ua.everybuy.routing.dto.mapper.SubCategoryMapper;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final SubCategoryMapper mapper;
    private final CategoryService categoryService;

    public SubCategory findById(Long id) {
        return subCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subcategory not found"));
    }

    public List<SubCategoryDto> findTopLevelSubCategoriesByCategoryId(Long categoryId) {
        categoryService.findById(categoryId);
        List<SubCategory> subCategories = subCategoryRepository.findTopLevelSubCategoriesByCategoryId(categoryId);
        return subCategories.stream()
                .map(mapper::mapToSubCategoryDto)
                .collect(Collectors.toList());
    }

    public List<SubCategoryDto> findSubCategoriesByParentId(Long parentId) {
        List<SubCategory> subCategories = subCategoryRepository.findByParentCategoryId(parentId);
        return subCategories.stream()
                .map(mapper::mapToSubCategoryDto)
                .collect(Collectors.toList());
    }
}
