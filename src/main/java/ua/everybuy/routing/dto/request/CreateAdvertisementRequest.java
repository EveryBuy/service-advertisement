package ua.everybuy.routing.dto.request;

import jakarta.validation.constraints.*;
import ua.everybuy.database.entity.Advertisement;

import java.util.List;
import java.util.Set;

public record CreateAdvertisementRequest(
        @NotNull(message = "Product section is required")
        Advertisement.AdSection section,

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(min = 30, max = 3000, message = "Description must be between 30 and 3000 characters")
        String description,

        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price must be a positive number")
        Long price,

        @NotNull(message = "City ID is required")
        Long cityId,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        @NotNull(message = "Subcategory ID is required")
        Long topSubCategoryId,
        Long lowSubCategoryId,

        @NotNull(message = "Product type is required")
        Advertisement.ProductType productType,

        @NotNull(message = "Negotiable status is required")
        Boolean isNegotiable,

        @NotEmpty(message = "Delivery methods are required")
        Set<String> deliveryMethods,

        @NotEmpty(message = "Rotations are required")
        List< @Min(0) @Max(3)Byte> rotations

) implements CategoryRequest {
}
