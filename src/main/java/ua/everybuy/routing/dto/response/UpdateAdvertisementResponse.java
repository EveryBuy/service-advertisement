package ua.everybuy.routing.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UpdateAdvertisementResponse {
    private Long id;
    private String title;
    private String description;
    private Long price;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Boolean isEnabled;
    private Boolean isNegotiable;
    private Long userId;
    private String mainPhotoUrl;
    private List<String> photoUrls;
    private String cityName;
    private String categoryNameUkr;
    private String topSubCategoryNameUkr;
    private String lowSubCategoryNameUkr;
    private String productType;
    private String section;
    private Set<String> deliveryMethods;
}
