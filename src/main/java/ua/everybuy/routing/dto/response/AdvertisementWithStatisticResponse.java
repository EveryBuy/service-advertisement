package ua.everybuy.routing.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.routing.dto.util.PriceSerializer;

@Getter
@Setter
public class AdvertisementWithStatisticResponse {
    private Long id;
    private String section;
    private String title;
    private String productType;
    @JsonSerialize(using = PriceSerializer.class)
    private Double price;
    private Long userId;
    private String mainPhotoUrl;
    private Integer favouriteCount;
    private Integer views;
}
