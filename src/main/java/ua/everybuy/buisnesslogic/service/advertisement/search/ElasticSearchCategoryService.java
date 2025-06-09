package ua.everybuy.buisnesslogic.service.advertisement.search;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.errorhandling.custom.SearchServiceException;
import ua.everybuy.routing.dto.TopCategorySearchResultDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticSearchCategoryService {
    private static final String TOP_CATEGORIES_AGGREGATION = "topCategories";
    private static final String ADVERTISEMENTS_INDEX = "advertisements";
    private static final int MAX_AGGREGATION_SIZE = 20;

    private final RestHighLevelClient restHighLevelClient;
    private final TopLevelSubCategoryService topLevelSubCategoryService;

    public List<TopCategorySearchResultDto> findTopCategoriesByKeyword(String keyword, String section) {
        Map<Long, Long> categoryDistribution = getTopSubCategoryCountsFromElasticsearch(keyword, section);
        List<Long> ids = new ArrayList<>(categoryDistribution.keySet());

        List<TopCategorySearchResultDto> searchResultDto = topLevelSubCategoryService.getSearchResultDto(ids);
        return mergeCategoryCounts(searchResultDto, categoryDistribution);
    }

    private Map<Long, Long> getTopSubCategoryCountsFromElasticsearch(String keyword, String section) {
        try {
            SearchRequest searchRequest = buildSearchRequest(keyword, section);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return extractCounts(searchResponse);
        } catch (IOException e) {
            throw new SearchServiceException("Failed to search categories for keyword: " + keyword, e);
        }
    }

    private SearchRequest buildSearchRequest(String keyword, String section) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("title", keyword).fuzziness(Fuzziness.AUTO))
                .filter(QueryBuilders.termQuery("isEnabled", true));

        if (section != null && !section.isEmpty()) {
            boolQuery.filter(QueryBuilders.termQuery("section", section));
        }

        sourceBuilder.query(boolQuery);
        sourceBuilder.aggregation(
                AggregationBuilders.terms(TOP_CATEGORIES_AGGREGATION)
                        .field("topSubCategoryId")
                        .size(MAX_AGGREGATION_SIZE)
        );
        sourceBuilder.size(0);

        return new SearchRequest(ADVERTISEMENTS_INDEX).source(sourceBuilder);
    }

    private Map<Long, Long> extractCounts(SearchResponse response) {
        Map<Long, Long> categoryCount = new HashMap<>();
        Terms topCategories = response.getAggregations().get(TOP_CATEGORIES_AGGREGATION);

        if (topCategories != null) {
            topCategories.getBuckets().forEach(bucket ->
                    categoryCount.put(
                            Long.valueOf(bucket.getKeyAsString()),
                            bucket.getDocCount()
                    )
            );
        }
        return categoryCount;
    }

    private List<TopCategorySearchResultDto> mergeCategoryCounts(
            List<TopCategorySearchResultDto> dtos,
            Map<Long, Long> countMap) {
        return dtos.stream()
                .peek(dto -> dto.setCount(countMap.getOrDefault(dto.getTopCategoryId(), 0L)))
                .collect(Collectors.toList());
    }
}
