package ua.everybuy.routing.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.City;
import ua.everybuy.database.entity.SubCategory;

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
    private City city;
    private SubCategory subCategory;
    private String productType;
    private Set<Advertisement.DeliveryMethod> deliveryMethods;
    private ShortUserInfoDto userDto;
}
