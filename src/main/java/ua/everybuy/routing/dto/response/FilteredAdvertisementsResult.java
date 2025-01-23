package ua.everybuy.routing.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilteredAdvertisementsResult {
    private long totalAdvertisements;
    private int totalPages;
    private Double minPrice;
    private Double maxPrice;
    private List<FilteredAdvertisementsResponse> advertisements;
}
