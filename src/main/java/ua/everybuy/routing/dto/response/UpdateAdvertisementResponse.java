package ua.everybuy.routing.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.routing.dto.util.PriceSerializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UpdateAdvertisementResponse {
    private Long id;
    private String title;
    private String description;
    @JsonSerialize(using = PriceSerializer.class)
    private Double price;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Boolean isEnabled;
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
