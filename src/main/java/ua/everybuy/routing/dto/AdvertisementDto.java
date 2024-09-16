package ua.everybuy.routing.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.database.entity.Category;
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
    private String price;
    private LocalDateTime creationDate;
    private Boolean isEnabled;
    private Long userId;
    private String mainPhotoUrl;
    private List<String> photoUrls;
    private String cityName;
    private String regionName;
    private Category category;
    private SubCategoryDto topSubCategory;
    private SubCategoryDto lowSubCategory;
    private String productType;
    private String section;
    private Set<String> deliveryMethods;
    private ShortUserInfoDto userDto;
}
