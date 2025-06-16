package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.advertisement.search.category.ElasticSearchCategoryService;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.routing.dto.SubCategoryDto;
import ua.everybuy.routing.dto.TopCategorySearchResultDto;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/category")
public class TopLevelSubCategoryController {
    private final TopLevelSubCategoryService topLevelSubCategoryService;
    private final ElasticSearchCategoryService elasticSearchCategoryService;

    @GetMapping("/{categoryId}/top-level-subcategories")
    @ResponseStatus(HttpStatus.OK)
    public List<SubCategoryDto> getTopLevelSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        return topLevelSubCategoryService.getSubCategoriesByTopSubcategoryId(categoryId);

    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Map <String, List<TopCategorySearchResultDto>>getUniqueCategories(
            @RequestParam("keyword") String keyword) {
        return elasticSearchCategoryService.findTopCategoriesByKeyword(keyword);
    }
}
