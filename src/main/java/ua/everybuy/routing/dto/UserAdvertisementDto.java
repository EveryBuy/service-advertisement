package ua.everybuy.routing.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;
import java.util.List;

@Setter
@Getter
@Builder
public class UserAdvertisementDto {
    private ShortUserInfoDto user;
    private long totalAdvertisements;
    private int totalPages;
    private List<CategoryAdvertisementCountDto> categories;
    private List<FilteredAdvertisementsResponse> filteredAds;
}
