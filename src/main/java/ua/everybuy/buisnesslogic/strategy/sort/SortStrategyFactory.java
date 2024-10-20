package ua.everybuy.buisnesslogic.strategy.sort;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ua.everybuy.errorhandling.message.FilterAdvertisementValidationMessages;
import java.util.HashMap;
import java.util.Map;

@Service
public class SortStrategyFactory {
    private static final String PRICE_ASCENDING = "ASC";
    private static final String PRICE_DESCENDING = "DESC";
    private static final String DATE_ASCENDING = "DATE_ASC";
    public static final String DATE_DESCENDING = "DATE_DESC";
    private static final String DEFAULT_ORDER = "DEFAULT";
    private final Map<String, SortStrategy> sortStrategies = new HashMap<>();

    @PostConstruct
    public void init() {
        sortStrategies.put(PRICE_ASCENDING, new PriceAscendingSortStrategy());
        sortStrategies.put(PRICE_DESCENDING, new PriceDescendingSortStrategy());
        sortStrategies.put(DATE_ASCENDING, new DateAscendingSortStrategy());
        sortStrategies.put(DATE_DESCENDING, new DateDescendingSortStrategy());
        sortStrategies.put(DEFAULT_ORDER, new DefaultSortStrategy());
    }

    public SortStrategy getSortStrategy(String sortOrder) {
        if (sortOrder == null || sortOrder.isBlank()) {
            return sortStrategies.get(DEFAULT_ORDER);
        }

        SortStrategy sortStrategy = sortStrategies.get(sortOrder.toUpperCase());

        if (sortStrategy == null) {
            throw new IllegalArgumentException(FilterAdvertisementValidationMessages.INVALID_SORT_ORDER_MESSAGE);
        }
        return sortStrategy;
    }
}
