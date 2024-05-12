package ua.everybuy.routing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ua.everybuy.database.entity.Advertisement;

import java.util.Set;

public record UpdateAdvertisementRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 3000, message = "Description must be less than 1000 characters")
        String description,

        @NotBlank(message = "Price is required")
        @Size(max = 55, message = "Price must be less than 55 characters")
        String price,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "City ID is required")
        Long cityId,

        @NotNull(message = "Subcategory ID is required")
        Long subCategoryId,

        @NotNull(message = "Product type is required")
        Advertisement.ProductType productType,

        @NotNull(message = "Delivery methods are required")
        Set<Advertisement.DeliveryMethod> deliveryMethods


) {
}