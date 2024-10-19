package ua.everybuy.buisnesslogic.strategy.sort;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class SortStrategyFactory {
    private static final String ASCENDING_ORDER = "ASC";
    private static final String DESCENDING_ORDER = "DESC";
    private static final String DEFAULT_ORDER = "DEFAULT";
    private final Map<String, SortStrategy> sortStrategies = new HashMap<>();

    @PostConstruct
    public void init() {
        sortStrategies.put(ASCENDING_ORDER, new AscendingSortStrategy());
        sortStrategies.put(DESCENDING_ORDER, new DescendingSortStrategy());
        sortStrategies.put(DEFAULT_ORDER, new DescendingSortStrategy());
    }

    public SortStrategy getSortStrategy(String sortOrder) {
        if (sortOrder == null || sortOrder.isBlank()) {
            return sortStrategies.get(DEFAULT_ORDER);
        }

        SortStrategy sortStrategy = sortStrategies.get(sortOrder.toUpperCase());

        if (sortStrategy == null) {
            throw new IllegalArgumentException("Invalid sort order: " + sortOrder);
        }
        return sortStrategy;
    }
}
