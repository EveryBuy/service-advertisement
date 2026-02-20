package ua.everybuy.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

@Component
public class EnabledFilter implements SearchFilter<Boolean> {
    @Override
    public void apply(BoolQueryBuilder query, Boolean value) {
        query.filter(QueryBuilders.termQuery("isEnabled", value));
    }
}
