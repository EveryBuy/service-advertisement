package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.category.LowLevelSubCategoryService;
import ua.everybuy.routing.dto.SubCategoryDto;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ad/subcategory")
public class LowLevelSubCategoryController {
    private final LowLevelSubCategoryService subCategoryService;
    @GetMapping("/{subcategoryId}/low-level-subcategories")
    @ResponseStatus(HttpStatus.OK)
    public List<SubCategoryDto> getSubCategoriesByParentId(@PathVariable Long subcategoryId) {
        return subCategoryService.getSubCategoriesByCategoryId(subcategoryId);
    }
}
