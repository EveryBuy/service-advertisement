package ua.everybuy.buisnesslogic.service.advertisement.search;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.stereotype.Component;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

@Component
public class ElasticSearchAdvertisementFilterAppender {
    public void appendKeywordQuery(BoolQueryBuilder boolQuery, String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            boolQuery.should(QueryBuilders.matchQuery("title", keyword).fuzziness(Fuzziness.AUTO));
            boolQuery.should(QueryBuilders.prefixQuery("title", keyword.toLowerCase()));
            boolQuery.minimumShouldMatch(1);
        }
    }

    public void appendTermFilter(BoolQueryBuilder query, String field, Object value) {
        if (value != null) {
            query.filter(QueryBuilders.termQuery(field, value));
        }
    }

    public void appendPriceFilter(BoolQueryBuilder query, AdvertisementSearchParametersDto dto) {
        if (dto.getMinPrice() != null || dto.getMaxPrice() != null) {
            RangeQueryBuilder range = QueryBuilders.rangeQuery("price");
            if (dto.getMinPrice() != null) range.gte(dto.getMinPrice());
            if (dto.getMaxPrice() != null) range.lte(dto.getMaxPrice());
            query.filter(range);
        }
    }

    public void appendEnabledFilter(BoolQueryBuilder boolQuery) {
        boolQuery.filter(QueryBuilders.termQuery("isEnabled", true));
    }
}
