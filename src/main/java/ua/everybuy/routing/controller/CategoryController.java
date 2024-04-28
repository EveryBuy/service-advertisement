package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.buisnesslogic.service.CategoryService;
import ua.everybuy.database.entity.Category;
import ua.everybuy.routing.dto.StatusResponse;

import java.util.List;

@RestController
@RequestMapping("/ad/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategory() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/list/ukr")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> getAllUkrNames() {
        List<String> ukrCategories = categoryService.getAllUkrCategories();
        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(ukrCategories)
                .build());
    }
}
