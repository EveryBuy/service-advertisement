package ua.everybuy.routing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FavouriteAdvertisementResponse {
    private long id;
    private long userId;
    private long advertisementId;
}
