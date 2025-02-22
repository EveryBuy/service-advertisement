package ua.everybuy.routing.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.everybuy.database.entity.Advertisement;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementSearchParametersDto {
    @Min(0)
    private Double minPrice;
    @Min(0)
    private Double maxPrice;
    private Long regionId;
    private Long cityId;
    private Long topSubCategoryId;
    private Long lowSubCategoryId;
    private Long categoryId;
    private Advertisement.AdSection section = Advertisement.AdSection.SELL;
    private Advertisement.ProductType productType;
    private String keyword;
    private String sortOrder;
}
// @RequestParam(required = false) @Valid @Min(0) Double minPrice,
//            @RequestParam(required = false) @Valid @Min(0) Double maxPrice,
//            @RequestParam(required = false) @Valid String sortOrder,
//            @RequestParam(required = false) @Valid Long regionId,
//            @RequestParam(required = false) @Valid Long cityId,
//            @RequestParam(required = false) @Valid Long topSubCategoryId,
//            @RequestParam(required = false) @Valid Long lowSubCategoryId,
//            @RequestParam(required = false) @Valid Long categoryId,
//            @RequestParam(required = false) @Valid Advertisement.ProductType productType,
//            @RequestParam(required = false, defaultValue = "SELL") @Valid Advertisement.AdSection section,
//            @RequestParam(required = false) @Valid String keyword,

