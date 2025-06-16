package ua.everybuy.buisnesslogic.service.advertisement.search;

import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.springframework.stereotype.Service;
import ua.everybuy.routing.dto.PriceRangeDto;

@Service
public class PriceAggregationExtractor {
    public PriceRangeDto extractPriceRange(Aggregations aggregations) {
        if (aggregations == null) return new PriceRangeDto(0.0, 0.0);

        return PriceRangeDto.builder()
                .minPrice(extractMinPrice(aggregations))
                .maxPrice(extractMaxPrice(aggregations))
                .build();
    }

    private double extractMinPrice(Aggregations aggregations) {
        Min min = aggregations.get("min_price");
        if (min == null) return 0.0;

        double value = min.getValue();
        return (!Double.isInfinite(value) && !Double.isNaN(value)) ? value : 0.0;
    }

    private double extractMaxPrice(Aggregations aggregations) {
        Max max = aggregations.get("max_price");
        if (max == null) return 0.0;

        double value = max.getValue();
        return (!Double.isInfinite(value) && !Double.isNaN(value)) ? value : 0.0;
    }
}
