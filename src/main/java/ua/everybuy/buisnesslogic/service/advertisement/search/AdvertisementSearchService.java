package ua.everybuy.buisnesslogic.service.advertisement.search;

import ua.everybuy.routing.dto.AdvertisementSearchResultDto;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

public interface AdvertisementSearchService {
    AdvertisementSearchResultDto searchAdvertisements(AdvertisementSearchParametersDto searchDto, int page, int size);
}
