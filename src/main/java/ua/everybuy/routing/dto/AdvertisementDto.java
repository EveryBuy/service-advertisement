package ua.everybuy.routing.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.entity.City;
import ua.everybuy.routing.dto.util.PriceSerializer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class AdvertisementDto {
    private Long id;
    private String title;
    private String description;
    @JsonSerialize(using = PriceSerializer.class)
    private Double price;
    private LocalDateTime creationDate;
    private Boolean isEnabled;
    private Boolean isNegotiable;
    private Long userId;
    private String mainPhotoUrl;
    private List<String> photoUrls;
    private String productType;
    private String section;
    private City city;
    private Category category;
    private SubCategoryDto topSubCategory;
    private SubCategoryDto lowSubCategory;
    private Set<String> deliveryMethods;
    private ShortUserInfoDto userDto;
}
