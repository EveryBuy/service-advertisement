package ua.everybuy.routing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.everybuy.buisnesslogic.service.advertisement.search.ElasticSearchCategoryService;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.routing.dto.SubCategoryDto;
import ua.everybuy.routing.dto.TopCategorySearchResultDto;

import java.util.List;

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
    public List<TopCategorySearchResultDto> getUniqueCategories(
            @RequestParam("keyword") String keyword,
            @RequestParam(required = false) Advertisement.AdSection section) {
        String sectionName = section != null ? section.name() : null;

        return elasticSearchCategoryService.findTopCategoriesByKeyword(keyword, sectionName);
    }
}
