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
import ua.everybuy.routing.dto.TopCategorySearchResultDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElasticSearchCategoryService {
    private final RestHighLevelClient restHighLevelClient;
    private final TopLevelSubCategoryService topLevelSubCategoryService;

    public List<TopCategorySearchResultDto> findTopCategoriesByKeyword(String keyword) {
        Map<Long, Long> categoryDistribution = getTopSubCategoryCountsFromElasticsearch(keyword);

        List<Long> ids = new ArrayList<>(categoryDistribution.keySet());
        return topLevelSubCategoryService.getSearchResultDto(ids, categoryDistribution);
    }

    private Map<Long, Long> getTopSubCategoryCountsFromElasticsearch(String keyword) {
        try {
            SearchRequest searchRequest = buildSearchRequest(keyword);
            SearchResponse searchResponse = restHighLevelClient
                    .search(searchRequest, RequestOptions.DEFAULT);
            return extractCounts(searchResponse);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute aggregation query", e);
        }
    }

    private SearchRequest buildSearchRequest(String keyword) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("title", keyword).fuzziness(Fuzziness.AUTO))
                .filter(QueryBuilders.termQuery("isEnabled", true));

        sourceBuilder.query(boolQuery);
        sourceBuilder.aggregation(
                AggregationBuilders.terms("topCategories")
                        .field("topSubCategoryId")
                        .size(20)
        );
        sourceBuilder.size(0);

        SearchRequest searchRequest = new SearchRequest("advertisements");
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }

    private Map<Long, Long> extractCounts(SearchResponse response) {
        Map<Long, Long> categoryCount = new HashMap<>();

        Terms topCategories = response.getAggregations().get("topCategories");
        if (topCategories != null) {
            for (Terms.Bucket bucket : topCategories.getBuckets()) {
                Long key = Long.valueOf(bucket.getKeyAsString());
                categoryCount.put(key, bucket.getDocCount());
            }
        }
        return categoryCount;
    }
}
