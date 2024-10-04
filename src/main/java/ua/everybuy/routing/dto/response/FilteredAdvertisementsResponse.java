package ua.everybuy.routing.dto.response;

import lombok.Getter;
import lombok.Setter;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.entity.City;
import ua.everybuy.routing.dto.SubCategoryDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class FilteredAdvertisementsResponse {
    private Long advertisementId;
    private Long userId;
    private String mainPhotoUrl;
    private String title;
    private Advertisement.ProductType productType;
    private String price;
    private String description;
    private LocalDateTime updateDate;
    private City city;
    private SubCategoryDto topSubCategory;
    private SubCategoryDto lowSubCategory;
    private Category category;
}
