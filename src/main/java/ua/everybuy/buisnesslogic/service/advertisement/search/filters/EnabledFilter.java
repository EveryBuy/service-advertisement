package ua.everybuy.buisnesslogic.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

@Component
public class EnabledFilter implements SearchFilter<Object> {
    @Override
    public void apply(BoolQueryBuilder query, Object value) {
        query.filter(QueryBuilders.termQuery("isEnabled", true));
    }
}
