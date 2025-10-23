package ua.everybuy.routing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementInfoForChatService {
    private Long id;
    private String section;
    private Boolean isEnabled;
    private String title;
    private String price;
    private Long userId;
    private String mainPhotoUrl;
}
