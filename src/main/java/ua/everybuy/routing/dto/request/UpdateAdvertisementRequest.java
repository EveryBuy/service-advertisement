package ua.everybuy.routing.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ua.everybuy.database.entity.Advertisement;
import java.util.Set;

public record UpdateAdvertisementRequest(
        @NotNull(message = "Product section is required")
        Advertisement.AdSection section,
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 3000, message = "Description must be less than 1000 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be a positive number")
        Double price,

        @NotNull(message = "City ID is required")
        Long cityId,

        @NotNull(message = "Category ID is required")
        Long categoryId,
        @NotNull(message = "Subcategory ID is required")
        Long topSubCategoryId,
        Long lowSubCategoryId,
        @NotNull(message = "Product type is required")
        Advertisement.ProductType productType,

        @NotNull(message = "Delivery methods are required")
        Set<String> deliveryMethods

) implements CategoryRequest {
}
