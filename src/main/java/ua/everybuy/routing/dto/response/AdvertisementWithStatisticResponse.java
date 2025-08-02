package ua.everybuy.routing.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvertisementWithStatisticResponse {
    private Long id;
    private String section;
    private String title;
    private String productType;
    private Long price;
    private Long userId;
    private String mainPhotoUrl;
    private Integer favouriteCount;
    private Integer views;
}
