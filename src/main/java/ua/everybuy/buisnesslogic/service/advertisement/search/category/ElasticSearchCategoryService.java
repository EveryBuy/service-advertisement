package ua.everybuy.buisnesslogic.service.advertisement.search.category;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.stereotype.Service;
import ua.everybuy.buisnesslogic.service.category.TopLevelSubCategoryService;
import ua.everybuy.errorhandling.custom.SearchServiceException;
import ua.everybuy.routing.dto.TopCategorySearchResultDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticSearchCategoryService {
    private static final String CATEGORY_AGG = "topCategories";
    private static final String SECTION_AGG = "by_section";
    private static final List<String> SECTIONS = List.of("SELL", "BUY");

    private final ElasticSearchCategoryAggregationQueryBuilder queryBuilder;
    private final RestHighLevelClient esClient;
    private final TopLevelSubCategoryService topCategoryService;

    /**
     * Finds top categories by keyword, grouped by section (SELL/BUY)
     *
     * @param keyword Search term
     * @return Map where key is section, value is list of categories with counts
     */
    public Map<String, List<TopCategorySearchResultDto>> findTopCategoriesByKeyword(String keyword) {
        Map<String, Map<Long, Long>> sectionStats = getSectionStats(keyword);

        List<Long> categoryIds = getAllCategoryIds(sectionStats);

        List<TopCategorySearchResultDto> categories = topCategoryService
                .getSearchResultDto(categoryIds);

        return mapResultsBySection(sectionStats, categories);
    }

    private Map<String, Map<Long, Long>> getSectionStats(String keyword) {
        try {
            SearchRequest request = queryBuilder.buildSearchRequest(keyword, SECTIONS);
            SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);
            return parseResponse(response);
        } catch (IOException e) {
            throw new SearchServiceException("Search failed for: " + keyword, e);
        }
    }

    private Map<String, Map<Long, Long>> parseResponse(SearchResponse response) {
        Map<String, Map<Long, Long>> result = new HashMap<>();

        Terms sections = response.getAggregations().get(SECTION_AGG);
        if (sections == null) return result;

        sections.getBuckets().forEach(section -> {
            String sectionName = section.getKeyAsString();
            Terms categories = section.getAggregations().get(CATEGORY_AGG);

            Map<Long, Long> counts = new HashMap<>();
            if (categories != null) {
                categories.getBuckets()
                        .forEach(cat -> counts.put(
                                Long.valueOf(cat.getKeyAsString()),
                                cat.getDocCount()
                        ));
            }

            result.put(sectionName, counts);
        });

        SECTIONS.forEach(section -> result.putIfAbsent(section, Map.of()));

        return result;
    }

    private List<Long> getAllCategoryIds(Map<String, Map<Long, Long>> sectionStats) {
        return sectionStats.values().stream()
                .flatMap(map -> map.keySet().stream())
                .collect(Collectors.toList());
    }

    private Map<String, List<TopCategorySearchResultDto>> mapResultsBySection(
            Map<String, Map<Long, Long>> sectionStats,
            List<TopCategorySearchResultDto> categories) {

        Map<String, List<TopCategorySearchResultDto>> result = new HashMap<>();

        SECTIONS.forEach(section -> {
            Map<Long, Long> counts = sectionStats.get(section);

            List<TopCategorySearchResultDto> sectionCategories = categories.stream()
                    .filter(cat -> counts.containsKey(cat.getTopCategoryId()))
                    .map(cat -> TopCategorySearchResultDto.builder()
                            .categoryId(cat.getCategoryId())
                            .categoryName(cat.getCategoryName())
                            .topCategoryId(cat.getTopCategoryId())
                            .topCategoryName(cat.getTopCategoryName())
                            .count(counts.get(cat.getTopCategoryId()))
                            .build()
                    )
                    .collect(Collectors.toList());

            result.put(section, sectionCategories);
        });

        return result;
    }
}
