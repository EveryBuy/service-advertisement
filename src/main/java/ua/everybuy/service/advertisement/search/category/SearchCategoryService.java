package ua.everybuy.service.advertisement.search.category;

import ua.everybuy.routing.dto.TopCategorySearchResultDto;

import java.util.List;
import java.util.Map;

public interface SearchCategoryService {
    Map<String, List<TopCategorySearchResultDto>> findTopCategoriesByKeyword(String keyword);
}
