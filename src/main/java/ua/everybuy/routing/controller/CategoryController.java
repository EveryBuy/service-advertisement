package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseBody
    @CrossOrigin(origins = "*")
    public StatusResponse<List<Category>> getAllCategory() {
        List<Category> categoryList = categoryService.getAllCategories();
        return new StatusResponse<>(HttpStatus.OK.value(), categoryList);
    }

    @GetMapping("/ukr")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StatusResponse<List<String>> getAllUkrNames() {
        List<String> ukrCategories = categoryService.getAllUkrCategories();
        return new StatusResponse<>(HttpStatus.OK.value(), ukrCategories);
    }
}
