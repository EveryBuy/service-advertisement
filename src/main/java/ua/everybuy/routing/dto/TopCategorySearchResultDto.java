package ua.everybuy.routing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopCategorySearchResultDto {
    private Long categoryId;
    private String categoryName;
    private Long topCategoryId;
    private String topCategoryName;
    private Long count;
}
