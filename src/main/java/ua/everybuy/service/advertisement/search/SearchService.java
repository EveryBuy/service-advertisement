package ua.everybuy.service.advertisement.search;

import ua.everybuy.routing.dto.AdvertisementSearchResultDto;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

public interface SearchService {
    AdvertisementSearchResultDto searchAdvertisements(AdvertisementSearchParametersDto searchDto, int page, int size);
}
