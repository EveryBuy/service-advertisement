package ua.everybuy.routing.dto.request;

public interface CategoryRequest {
    Long categoryId();

    Long topSubCategoryId();

    Long lowSubCategoryId();
}
