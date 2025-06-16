package ua.everybuy.buisnesslogic.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.Advertisement;

@Component
public class ProductTypeFilter implements SearchFilter<Advertisement.ProductType> {
    @Override
    public void apply(BoolQueryBuilder query, Advertisement.ProductType productType) {
        if (productType != null) {
            query.filter(QueryBuilders.termQuery("productType", productType.name()));
        }
    }
}
