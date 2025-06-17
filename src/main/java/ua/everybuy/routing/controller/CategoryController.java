package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.service.category.CategoryService;
import ua.everybuy.database.entity.Category;
import java.util.List;

@RestController
@RequestMapping("/product/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/ukr")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllUkrNames() {
        return categoryService.getAllUkrCategories();
    }
}
