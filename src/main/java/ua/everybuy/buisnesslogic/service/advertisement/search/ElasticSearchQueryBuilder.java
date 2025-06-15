package ua.everybuy.buisnesslogic.service.advertisement.search;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import ua.everybuy.errorhandling.message.FilterAdvertisementValidationMessages;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

@Component
@RequiredArgsConstructor
public class ElasticSearchQueryBuilder {
    private static final String INDEX_NAME = "advertisements";
    private final ElasticSearchAdvertisementFilterAppender filterAppender;

    public SearchRequest buildSearchRequest(AdvertisementSearchParametersDto dto, int page, int size) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(buildBoolQuery(dto))
                .from((page - 1) * size)
                .size(size)
                .aggregation(AggregationBuilders.min("min_price").field("price"))
                .aggregation(AggregationBuilders.max("max_price").field("price"))
                .sort("creationDate", SortOrder.DESC);

        applyPriceSorting(dto.getSortOrder(), sourceBuilder);

        return new SearchRequest(INDEX_NAME).source(sourceBuilder);
    }

    private void applyPriceSorting(String sortOrder, SearchSourceBuilder sourceBuilder) {
        if (StringUtils.isBlank(sortOrder)) {
            return;
        }

        SortOrder order = parseSortOrder(sortOrder);
        sourceBuilder.sort("price", order);
    }

    private SortOrder parseSortOrder(String sortOrder) {
        return switch (sortOrder.toUpperCase()) {
            case "ASC" -> SortOrder.ASC;
            case "DESC" -> SortOrder.DESC;
            default -> throw new IllegalArgumentException(FilterAdvertisementValidationMessages.INVALID_SORT_ORDER_MESSAGE);
        };
    }

    private BoolQueryBuilder buildBoolQuery(AdvertisementSearchParametersDto dto) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        filterAppender.appendKeywordQuery(boolQuery, dto.getKeyword());
        filterAppender.appendTermFilter(boolQuery, "topSubCategoryId", dto.getTopSubCategoryId());
        filterAppender.appendTermFilter(boolQuery, "lowSubCategoryId", dto.getLowSubCategoryId());
        filterAppender.appendTermFilter(boolQuery, "cityId", dto.getCityId());
        filterAppender.appendTermFilter(boolQuery, "regionId", dto.getRegionId());
        filterAppender.appendTermFilter(boolQuery, "categoryId", dto.getCategoryId());
        filterAppender.appendTermFilter(boolQuery, "section", dto.getSection() != null ? dto.getSection().name() : null);
        filterAppender.appendTermFilter(boolQuery, "productType", dto.getProductType() != null ? dto.getProductType().name() : null);
        filterAppender.appendPriceFilter(boolQuery, dto);
        filterAppender.appendEnabledFilter(boolQuery);
        return boolQuery;
    }
}
