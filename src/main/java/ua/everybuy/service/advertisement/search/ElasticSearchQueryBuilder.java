package ua.everybuy.service.advertisement.search;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import ua.everybuy.service.advertisement.search.filters.processors.FilterAdvertisementProcessor;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

/*
    "from": 20 (skip first 20 documents)
    "size": 10 (show 10 documents)
 */

@Component
@RequiredArgsConstructor
public class ElasticSearchQueryBuilder implements QueryBuilder {
    private static final String INDEX_NAME = "advertisements";
    private final FilterAdvertisementProcessor filterAdvertisementProcessor;
    private final SortBuilder sortBuilder;

    @Override
    public SearchRequest buildSearchRequest(AdvertisementSearchParametersDto dto, int page, int size) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(buildBoolQuery(dto))
                .from((page - 1) * size)
                .size(size)
                .aggregation(AggregationBuilders.min("min_price").field("price"))
                .aggregation(AggregationBuilders.max("max_price").field("price"))
                .sort("creationDate", SortOrder.DESC);

        sortBuilder.applySorting(dto, sourceBuilder);
        return new SearchRequest(INDEX_NAME).source(sourceBuilder);
    }

    private BoolQueryBuilder buildBoolQuery(AdvertisementSearchParametersDto dto) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        filterAdvertisementProcessor.process(boolQuery, dto);
        return boolQuery;
    }
}
