package ua.everybuy.buisnesslogic.service.advertisement.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElasticSearchCategoryAggregationQueryBuilder {
    private static final String SECTION_FIELD = "section";
    private static final String CATEGORY_FIELD = "topSubCategoryId";
    private static final String IS_ENABLED_FIELD = "isEnabled";
    private static final String TITLE_FIELD = "title";
    private static final String INDEX_NAME = "advertisements";
    private static final String CATEGORY_AGG = "topCategories";
    private static final String SECTION_AGG = "by_section";

    private static final int MAX_RESULTS = 20;

    public SearchRequest buildSearchRequest(String keyword, List<String> sections) {
        BoolQueryBuilder query = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery(TITLE_FIELD, keyword)
                        .fuzziness(Fuzziness.AUTO))
                .filter(QueryBuilders.termQuery(IS_ENABLED_FIELD, true));

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

}
