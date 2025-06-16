package ua.everybuy.buisnesslogic.service.advertisement.search;

import io.micrometer.common.util.StringUtils;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import ua.everybuy.errorhandling.message.FilterAdvertisementValidationMessages;
import ua.everybuy.routing.dto.request.AdvertisementSearchParametersDto;

@Component
public class SortBuilder {
    public void applySorting(AdvertisementSearchParametersDto dto, SearchSourceBuilder sourceBuilder) {
        if (StringUtils.isNotBlank(dto.getSortOrder())) {
            SortOrder order = parseSortOrder(dto.getSortOrder());
            sourceBuilder.sort("price", order);
        }
    }

    private SortOrder parseSortOrder(String sortOrder) {
        return switch (sortOrder.toUpperCase()) {
            case "ASC" -> SortOrder.ASC;
            case "DESC" -> SortOrder.DESC;
            default -> throw new IllegalArgumentException(
                    FilterAdvertisementValidationMessages.INVALID_SORT_ORDER_MESSAGE);
        };
    }
}