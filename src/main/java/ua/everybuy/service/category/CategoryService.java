package ua.everybuy.service.category;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.repository.category.CategoryRepository;
import java.util.List;
import static ua.everybuy.errorhandling.message.CategoryValidationMessages.CATEGORY_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Cacheable(value = "categoryCache", key = "#id")
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND_MESSAGE + id));
    }

    @Cacheable(value = "categoriesCache")
    public List<Category> getAllCategories() {
        System.out.println("Cache categories");
        return categoryRepository.findAll();
    }

    public List<String> getAllUkrCategories() {
        return categoryRepository.findAllUkrCategories();
    }
}
