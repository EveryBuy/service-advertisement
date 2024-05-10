package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.buisnesslogic.service.CategoryService;
import ua.everybuy.buisnesslogic.service.SubCategoryService;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.entity.SubCategory;
import ua.everybuy.routing.dto.StatusResponse;

import java.util.List;

@RestController
@RequestMapping("/ad/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategory() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/ukr")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAllUkrNames() {
        List<String> ukrCategories = categoryService.getAllUkrCategories();
        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(ukrCategories)
                .build());
    }

    @GetMapping("/subcategory")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAllSubcategory() {
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(subCategories)
                .build());
    }
}
