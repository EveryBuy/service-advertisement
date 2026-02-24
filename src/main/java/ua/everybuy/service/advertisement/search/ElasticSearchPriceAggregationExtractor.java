package ua.everybuy.service.advertisement.search;

import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.springframework.stereotype.Service;
import ua.everybuy.routing.dto.PriceRangeDto;

@Service
public class ElasticSearchPriceAggregationExtractor {
    private static final String MIN_PRICE_AGG = "min_price";
    private static final String MAX_PRICE_AGG = "max_price";

    public PriceRangeDto extractPriceRange(Aggregations aggregations) {
        if (aggregations == null) return new PriceRangeDto(0L, 0L);

        return PriceRangeDto.builder()
                .minPrice(extractMinPrice(aggregations))
                .maxPrice(extractMaxPrice(aggregations))
                .build();
    }

    private long extractMinPrice(Aggregations aggregations) {
        Min min = aggregations.get(MIN_PRICE_AGG);
        if (min == null) return 0L;
        if (min.getValue() == Double.POSITIVE_INFINITY) return 0L;
        return (long) min.getValue();
    }

    private long extractMaxPrice(Aggregations aggregations) {
        Max max = aggregations.get(MAX_PRICE_AGG);
        if (max == null) return 0L;

        if (max.getValue() == Double.NEGATIVE_INFINITY) return 0L;

        return (long) max.getValue();
    }
}
