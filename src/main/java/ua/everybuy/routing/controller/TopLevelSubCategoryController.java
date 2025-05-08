package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.routing.dto.SubCategoryDto;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/category")
public class TopLevelSubCategoryController {
    private final TopLevelSubCategoryService topLevelSubCategoryService;

    @GetMapping("/{categoryId}/top-level-subcategories")
    @ResponseStatus(HttpStatus.OK)
    public List<SubCategoryDto> getTopLevelSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        return topLevelSubCategoryService.getSubCategoriesByTopSubcategoryId(categoryId);
    }
}
