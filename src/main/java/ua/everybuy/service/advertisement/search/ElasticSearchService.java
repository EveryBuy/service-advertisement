package ua.everybuy.service.advertisement.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;
import ua.everybuy.errorhandling.custom.SearchServiceException;
import ua.everybuy.service.advertisement.filter.FilterValidator;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.routing.dto.AdvertisementSearchResultDto;
import ua.everybuy.routing.dto.PriceRangeDto;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;
import ua.everybuy.routing.mapper.AdvertisementDocumentMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ElasticSearchService implements SearchService {
    private final RestHighLevelClient client;
    private final QueryBuilder searchQueryBuilder;
    private final AdvertisementDocumentMapper mapper;
    private final ObjectMapper objectMapper;
    private final FilterValidator validator;
    private final ElasticSearchPriceAggregationExtractor elasticSearchPriceAggregationExtractor;

    @Override
    public AdvertisementSearchResultDto searchAdvertisements(AdvertisementSearchParametersDto searchDto, int page, int size) {
        validator.validate(searchDto);
        try {
            SearchRequest request = searchQueryBuilder.buildSearchRequest(searchDto, page, size);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            return buildSearchResultDto(response, size);

        } catch (IOException e) {
            throw new SearchServiceException("Elasticsearch search failed", e);
        }
    }

    private List<FilteredAdvertisementsResponse> parseSearchHits(SearchResponse response) {
        return Arrays.stream(response.getHits().getHits())
                .map(this::deserializeHit)
                .map(mapper::mapToFilteredAdvertisementsResponse)
                .collect(Collectors.toList());
    }

    private AdvertisementDocument deserializeHit(SearchHit hit) {
        try {
            return objectMapper.readValue(hit.getSourceRef().streamInput(), AdvertisementDocument.class);
        } catch (IOException e) {
            log.error("Failed to deserialize document ID: {}", hit.getId());
            throw new IllegalStateException("Data corruption in search index", e);
        }
    }

    private AdvertisementSearchResultDto buildSearchResultDto(SearchResponse response, int size) {

        PriceRangeDto priceRange = elasticSearchPriceAggregationExtractor
                .extractPriceRange(response.getAggregations());

        List<FilteredAdvertisementsResponse> ads = parseSearchHits(response);

        long totalHits = Objects.requireNonNull(response.getHits().getTotalHits()).value;
        int totalPages = (size > 0) ? (int) Math.ceil((double) totalHits / size) : 0;

        return AdvertisementSearchResultDto.builder()
                .totalAdvertisements(totalHits)
                .totalPages(totalPages)
                .minPrice(priceRange.getMinPrice())
                .maxPrice(priceRange.getMaxPrice())
                .advertisements(ads)
                .build();
    }
}
