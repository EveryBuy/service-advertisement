package ua.everybuy.buisnesslogic.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<String> getAllUkrCategories() {
        return categoryRepository.findAllUkrCategories();
    }
}