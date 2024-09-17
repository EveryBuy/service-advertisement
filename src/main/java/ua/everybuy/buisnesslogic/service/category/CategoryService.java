package ua.everybuy.buisnesslogic.service.category;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.repository.CategoryRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }
    @Cacheable(value = "categoriesCache")
    public List<Category> getAllCategories() {
        System.out.println("Cache");
        return categoryRepository.findAll();
    }

    public List<String> getAllUkrCategories() {
        return categoryRepository.findAllUkrCategories();
    }
}
