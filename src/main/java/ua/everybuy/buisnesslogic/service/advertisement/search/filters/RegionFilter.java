package ua.everybuy.buisnesslogic.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

@Component
public class RegionFilter implements SearchFilter<Long> {
    @Override
    public void apply(BoolQueryBuilder query, Long regionId) {
        if (regionId != null) {
            query.filter(QueryBuilders.termQuery("regionId", regionId));
        }
    }
}
