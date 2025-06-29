package ua.everybuy.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.stereotype.Component;

@Component
public class KeywordFilter implements SearchFilter<String> {

    @Override
    public void apply(BoolQueryBuilder query, String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            keyword = keyword.toLowerCase();
            query.should(QueryBuilders.termQuery("title.exact", keyword).boost(5.0f));

            if (keyword.matches(".*\\d+.*")) {
                query.should(QueryBuilders.matchQuery("title", keyword)
                        .analyzer("ukrainian")
                        .operator(Operator.AND)
                        .boost(4.0f));
            } else {
                query.should(QueryBuilders.matchQuery("title", keyword)
                        .analyzer("ukrainian")
                        .fuzziness(Fuzziness.ONE)
                        .boost(3.0f));

                query.should(QueryBuilders.matchPhraseQuery("title", keyword).boost(4.0f));
            }

            query.minimumShouldMatch(1);
        }
    }
}
