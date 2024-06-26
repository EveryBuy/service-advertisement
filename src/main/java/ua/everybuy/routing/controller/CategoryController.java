package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.CategoryService;
import ua.everybuy.database.entity.Category;
import ua.everybuy.routing.dto.response.StatusResponse;

import java.util.List;

@RestController
@RequestMapping("/ad/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*")
    public ResponseEntity<StatusResponse> getAllCategory() {
        List<Category> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(StatusResponse.builder()
                .status(HttpStatus.OK.value())
                .data(categoryList)
                .build());
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


}
