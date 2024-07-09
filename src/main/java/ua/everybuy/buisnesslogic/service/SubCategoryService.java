package ua.everybuy.buisnesslogic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.SubCategory;
import ua.everybuy.database.repository.SubCategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;

    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }
    public SubCategory findById(Long id) {
        return subCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Subcategory not found"));
    }
}
