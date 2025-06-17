package ua.everybuy.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.stereotype.Component;
import ua.everybuy.routing.dto.PriceRangeDto;

@Component
public class PriceFilter implements SearchFilter<PriceRangeDto> {
    @Override
    public void apply(BoolQueryBuilder query, PriceRangeDto dto) {
        if (dto.getMinPrice() != null || dto.getMaxPrice() != null) {
            RangeQueryBuilder range = QueryBuilders.rangeQuery("price");
            if (dto.getMinPrice() != null) range.gte(dto.getMinPrice());
            if (dto.getMaxPrice() != null) range.lte(dto.getMaxPrice());
            query.filter(range);
        }
    }
}

