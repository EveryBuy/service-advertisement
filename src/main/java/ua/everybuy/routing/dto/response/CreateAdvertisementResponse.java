package ua.everybuy.routing.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CreateAdvertisementResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDateTime creationDate;
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
