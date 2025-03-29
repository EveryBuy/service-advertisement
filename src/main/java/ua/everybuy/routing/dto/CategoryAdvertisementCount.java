package ua.everybuy.routing.dto;

import ua.everybuy.database.entity.Category;

public record CategoryAdvertisementCount(Category category, Long count) {
}
