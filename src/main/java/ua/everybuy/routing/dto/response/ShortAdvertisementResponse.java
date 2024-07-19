package ua.everybuy.routing.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortAdvertisementResponse {
    private Long id;
    private String title;
    private String price;
    private Long userId;
    private String mainPhotoUrl;
    private Integer favouriteCount;
    private Integer views;
}
