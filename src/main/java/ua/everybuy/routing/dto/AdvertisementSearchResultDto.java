package ua.everybuy.routing.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;

@Getter
@Setter
public class AdvertisementSearchResultDto {
    private long totalAdvertisements;
    private int totalPages;
    private Double minPrice;
    private Double maxPrice;
    private List<FilteredAdvertisementsResponse> advertisements;
}
