package ua.everybuy.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

@Component
public class TopSubCategoryFilter implements SearchFilter<Long> {
    @Override
    public void apply(BoolQueryBuilder query, Long topCategoryId) {
        if (topCategoryId != null) {
            query.filter(QueryBuilders.termQuery("topSubCategoryId", topCategoryId));
        }
    }
}
