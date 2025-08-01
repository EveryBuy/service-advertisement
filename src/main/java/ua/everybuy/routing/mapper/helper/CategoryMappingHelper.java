package ua.everybuy.routing.mapper.helper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ua.everybuy.service.category.SubCategoryService;
import ua.everybuy.database.entity.LowLevelSubCategory;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.request.CategoryRequest;

@Component
@RequiredArgsConstructor
public class CategoryMappingHelper {
    private final SubCategoryService subCategoryService;

    @Named("getTopLevelSubCategory")
    public <T extends CategoryRequest> TopLevelSubCategory getTopLevelSubCategory(T request) {
        return subCategoryService.getTopLevelSubCategory(request);
    }

    @Named("getLowLevelSubCategory")
    public <T extends CategoryRequest> LowLevelSubCategory getLowLevelSubCategory(T request) {
        return subCategoryService.getLowLevelSubCategory(request);
    }
}
