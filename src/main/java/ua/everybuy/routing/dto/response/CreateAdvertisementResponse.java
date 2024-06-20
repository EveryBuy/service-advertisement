package ua.everybuy.routing.dto.response;

import lombok.Getter;
import lombok.Setter;
import ua.everybuy.database.entity.Advertisement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CreateAdvertisementResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private LocalDateTime creationDate;
    private Boolean isEnabled;
    private Long userId;
    private String mainPhotoUrl;
    private List<String> photoUrls;
    private String cityName;
    private String subCategoryName;
    private String productType;
    private Set<Advertisement.DeliveryMethod> deliveryMethods;
}
