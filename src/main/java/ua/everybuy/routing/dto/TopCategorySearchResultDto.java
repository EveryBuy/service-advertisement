package ua.everybuy.routing.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopCategorySearchResultDto {
    private Long categoryId;
    private String categoryName;
    private Long topCategoryId;
    private String topCategoryName;
    private Long count;
}
