package ua.everybuy.buisnesslogic.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.Advertisement;

@Component
public class SectionFilter implements SearchFilter<Advertisement.AdSection> {
    @Override
    public void apply(BoolQueryBuilder query, Advertisement.AdSection section) {
        if (section == null) {
            query.filter(QueryBuilders.termQuery("section", "SELL"));
        } else {
            query.filter(QueryBuilders.termQuery("section",section.name()));
        }
    }
}
