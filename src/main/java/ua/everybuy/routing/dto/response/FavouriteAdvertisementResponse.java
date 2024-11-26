package ua.everybuy.routing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.Category;
import ua.everybuy.database.entity.City;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FavouriteAdvertisementResponse {
    private Long userId;
    private Long advertisementId;
    private String section;
    private String mainPhotoUrl;
    private String title;
    private Advertisement.ProductType productType;
    private String price;
    private LocalDateTime updateDate;
    private Category category;
    private City city;
}
