package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.SubCategoryService;
import ua.everybuy.routing.dto.SubCategoryDto;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ad")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @GetMapping("/category/{categoryId}/top-level-subcategories")
    @ResponseStatus(HttpStatus.OK)
    public List<SubCategoryDto> getTopLevelSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        return subCategoryService.findTopLevelSubCategoriesByCategoryId(categoryId);
    }
    @GetMapping("/subcategory/{subcategoryId}/low-level-subcategories")
    @ResponseStatus(HttpStatus.OK)
    public List<SubCategoryDto> getSubCategoriesByParentId(@PathVariable Long subcategoryId) {
        return subCategoryService.findSubCategoriesByParentId(subcategoryId);
    }
}
