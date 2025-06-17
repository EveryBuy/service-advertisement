package ua.everybuy.service.advertisement.search;

import org.elasticsearch.action.search.SearchRequest;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

public interface QueryBuilder {
    SearchRequest buildSearchRequest(AdvertisementSearchParametersDto dto, int page, int size);
}
