package ua.everybuy.routing.dto.response;

import lombok.Getter;
import lombok.Setter;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.City;
import ua.everybuy.routing.dto.CategoryDto;
import ua.everybuy.routing.dto.SubCategoryDto;

@Getter
@Setter
public class FilteredAdvertisementsResponse {
    private Long advertisementId;
    private Long userId;
    private String mainPhotoUrl;
    private String title;
    private Advertisement.ProductType productType;
    private String section;
    private Long price;
    private String description;
    private Boolean isNegotiable;
    private String updateDate;
    private City city;
    private SubCategoryDto topSubCategory;
    private SubCategoryDto lowSubCategory;
    private CategoryDto category;
}
