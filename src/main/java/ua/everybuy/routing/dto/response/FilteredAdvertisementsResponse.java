package ua.everybuy.routing.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.City;
import ua.everybuy.routing.dto.CategoryDto;
import ua.everybuy.routing.dto.SubCategoryDto;
import ua.everybuy.routing.dto.util.PriceSerializer;

import java.time.LocalDateTime;

@Getter
@Setter
public class FilteredAdvertisementsResponse {
    private Long advertisementId;
    private Long userId;
    private String mainPhotoUrl;
    private String title;
    private Advertisement.ProductType productType;
    private String section;
    @JsonSerialize(using = PriceSerializer.class)
    private Double price;
    private String description;
    private Boolean isNegotiable;
    private LocalDateTime updateDate;
    private City city;
    private SubCategoryDto topSubCategory;
    private SubCategoryDto lowSubCategory;
    private CategoryDto category;
}
