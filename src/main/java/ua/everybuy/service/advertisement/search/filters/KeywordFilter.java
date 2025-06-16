package ua.everybuy.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.stereotype.Component;

@Component
public class KeywordFilter implements SearchFilter<String> {
    @Override
    public void apply(BoolQueryBuilder query, String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            query.should(QueryBuilders.matchQuery("title", keyword).fuzziness(Fuzziness.AUTO));
            query.should(QueryBuilders.prefixQuery("title", keyword.toLowerCase()));
            query.minimumShouldMatch(1);
        }
    }
}
