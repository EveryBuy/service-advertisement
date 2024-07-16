package ua.everybuy.routing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AddToFavouriteResponse {
    private Long id;
    private Long userId;
    private Long advertisementId;
}
