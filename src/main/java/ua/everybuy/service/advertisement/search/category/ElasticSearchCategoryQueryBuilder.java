package ua.everybuy.service.advertisement.search.category;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;
import ua.everybuy.service.advertisement.search.filters.processors.FilterAdvertisementCategoryProcessor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ElasticSearchCategoryQueryBuilder implements QueryBuilder {
    private static final String SECTION_FIELD = "section";
    private static final String CATEGORY_FIELD = "topSubCategoryId";
    private static final String INDEX_NAME = "advertisements";
    private static final String CATEGORY_AGG = "topCategories";
    private static final String SECTION_AGG = "by_section";
    private static final int MAX_RESULTS = 100;
    private final FilterAdvertisementCategoryProcessor filterCategoryProcessor;

    public SearchRequest buildSearchRequest(String keyword, List<String> sections) {
        BoolQueryBuilder query = buildBoolQuery(keyword);

        TermsAggregationBuilder sectionAgg = AggregationBuilders.terms(SECTION_AGG)
                .field(SECTION_FIELD)
                .size(sections.size())
                .subAggregation(
                        AggregationBuilders.terms(CATEGORY_AGG)
                                .field(CATEGORY_FIELD)
                                .size(MAX_RESULTS)
                );

        SearchSourceBuilder source = new SearchSourceBuilder()
                .query(query)
                .aggregation(sectionAgg)
                .size(0);
        return new SearchRequest(INDEX_NAME).source(source);
    }

    private BoolQueryBuilder buildBoolQuery(String keyword) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        filterCategoryProcessor.process(boolQuery,keyword);
        return boolQuery;
    }

}
