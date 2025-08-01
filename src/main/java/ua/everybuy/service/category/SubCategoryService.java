package ua.everybuy.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.request.CategoryRequest;

@Service
@RequiredArgsConstructor
public class SubCategoryService {
    private final TopLevelSubCategoryService topSubCategoryService;
    private final LowLevelSubCategoryService lowSubCategoryService;

    public <T extends CategoryRequest> TopLevelSubCategory getTopLevelSubCategory(T request) {
        return topSubCategoryService.findTopLevelSubCategoryByCategoryAndSubCategoryId(
                request.categoryId(), request.topSubCategoryId());
    }

    public <T extends CategoryRequest> LowLevelSubCategory getLowLevelSubCategory(T request) {
        if (request.lowSubCategoryId() != null) {
            return lowSubCategoryService.findLowLevelSubCategoryByTopLevelId(
                    request.topSubCategoryId(), request.lowSubCategoryId());
        }
        return null;
    }
}
