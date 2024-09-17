package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.category.CategoryService;
import ua.everybuy.database.entity.Category;
import java.util.List;

@RestController
@RequestMapping("/ad/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/ukr")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getAllUkrNames() {
        return categoryService.getAllUkrCategories();
    }
}
