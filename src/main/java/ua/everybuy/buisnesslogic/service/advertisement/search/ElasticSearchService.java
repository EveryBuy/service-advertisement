package ua.everybuy.buisnesslogic.service.advertisement.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.advertisement.filter.FilterValidator;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.routing.dto.AdvertisementSearchResultDto;
import ua.everybuy.routing.dto.PriceRangeDto;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;
import ua.everybuy.routing.dto.response.FilteredAdvertisementsResponse;
import ua.everybuy.routing.mapper.AdvertisementDocumentMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticSearchService implements AdvertisementSearchService {
    private final RestHighLevelClient client;
    private final QueryBuilder searchQueryBuilder;
    private final AdvertisementDocumentMapper mapper;
    private final ObjectMapper objectMapper;
    private final FilterValidator validator;
    private final PriceAggregationExtractor priceAggregationExtractor;

    @Override
    public AdvertisementSearchResultDto searchAdvertisements(AdvertisementSearchParametersDto searchDto, int page, int size) {
        validator.validate(searchDto);
        try {
            SearchRequest request = searchQueryBuilder.buildSearchRequest(searchDto, page, size);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            List<FilteredAdvertisementsResponse> advertisements = parseSearchHits(response);
            long totalHits = response.getHits().getTotalHits().value;
            PriceRangeDto priceRange = priceAggregationExtractor.extractPriceRange(response.getAggregations());

            return buildSearchResultDto(advertisements, totalHits, size, priceRange);

        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch search failed", e);
        }
    }

    private List<FilteredAdvertisementsResponse> parseSearchHits(SearchResponse response) {
        return Arrays.stream(response.getHits().getHits())
                .map(hit -> {
                    try {
                        AdvertisementDocument doc = objectMapper.readValue(hit.getSourceAsString(), AdvertisementDocument.class);
                        return mapper.mapToFilteredAdvertisementsResponse(doc);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse search hit", e);
                    }
                })
                .collect(Collectors.toList());
    }

    private AdvertisementSearchResultDto buildSearchResultDto(List<FilteredAdvertisementsResponse> ads,
                                                              long totalHits, int size, PriceRangeDto priceRange) {
        return AdvertisementSearchResultDto.builder()
                .totalAdvertisements(totalHits)
                .totalPages((int) Math.ceil((double) totalHits / size))
                .minPrice(priceRange.getMinPrice())
                .maxPrice(priceRange.getMaxPrice())
                .advertisements(ads)
                .build();
    }
}
