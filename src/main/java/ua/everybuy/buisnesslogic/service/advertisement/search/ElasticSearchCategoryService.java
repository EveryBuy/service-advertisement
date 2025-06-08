package ua.everybuy.buisnesslogic.service.advertisement.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.database.entity.TopLevelSubCategory;
import ua.everybuy.routing.dto.TopCategorySearchResultDto;
import ua.everybuy.routing.mapper.SubCategoryMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElasticSearchCategoryService {
    private final ElasticsearchClient elasticsearchClient;
    private final TopLevelSubCategoryService topLevelSubCategoryService;
    private final SubCategoryMapper subCategoryMapper;

    public List<TopCategorySearchResultDto> findTopCategoriesByKeyword(String keyword) {
        Map<Long, Long> categoryDistribution = getTopSubCategoryCountsFromElasticsearch(keyword);

        List<TopLevelSubCategory> subCategories = topLevelSubCategoryService
                .getTopCategoriesById(new ArrayList<>(categoryDistribution.keySet()));

        return subCategories.stream()
                .map(subCategory -> {
                    Long count = categoryDistribution.getOrDefault(subCategory.getId(), 0L);
                    return subCategoryMapper.mapToTopCategoryUniqueDto(subCategory, count);
                })
                .toList();
    }

    private Map<Long, Long> getTopSubCategoryCountsFromElasticsearch(String keyword) {
        try {
            SearchResponse<Void> response = elasticsearchClient.search(buildSearchRequest(keyword), Void.class);
            return extractCounts(response);

        } catch (IOException e) {
            throw new RuntimeException("Failed to execute aggregation query", e);
        }
    }

    private Map<Long, Long> extractCounts(SearchResponse<Void> response) {
        Map<Long, Long> categoryCount = new HashMap<>();
        response.aggregations()
                .get("topCategories")
                .lterms()
                .buckets()
                .array()
                .forEach(bucket ->
                        categoryCount.put(bucket.key(), bucket.docCount())
                );
        return categoryCount;
    }

    private SearchRequest buildSearchRequest(String keyword) {
        return new SearchRequest.Builder()
                .index("advertisements")
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .match(mq -> mq
                                                .field("title")
                                                .query(keyword)
                                                .fuzziness("AUTO")
                                        )
                                )
                                .filter(f -> f
                                        .term(t -> t
                                                .field("isEnabled")
                                                .value(true)
                                        )
                                )
                        )
                )
                .aggregations("topCategories", a -> a
                        .terms(t -> t
                                .field("topSubCategoryId")
                                .size(20)
                        )
                )
                .build();
    }
}
