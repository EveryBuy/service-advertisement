package ua.everybuy.routing.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AdvertisementStatusResponse {
    private Long advertisementId;
    private Boolean status;
    private LocalDateTime updateDate;

}
