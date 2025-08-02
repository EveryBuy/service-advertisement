package ua.everybuy.routing.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;

@Getter
@Setter
@Builder
public class AdvertisementSearchResultDto {
    private long totalAdvertisements;
    private int totalPages;
    private Long minPrice;
    private Long maxPrice;
    private List<FilteredAdvertisementsResponse> advertisements;
}
