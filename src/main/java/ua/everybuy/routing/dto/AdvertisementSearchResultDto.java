package ua.everybuy.routing.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;
import ua.everybuy.routing.dto.util.PriceSerializer;

@Getter
@Setter
public class AdvertisementSearchResultDto {
    private List<CategoryDto> categories;
    private long totalAdvertisements;
    private int totalPages;
    @JsonSerialize(using = PriceSerializer.class)
    private Double minPrice;
    @JsonSerialize(using = PriceSerializer.class)
    private Double maxPrice;
    private List<FilteredAdvertisementsResponse> advertisements;
}
