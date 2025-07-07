package ua.everybuy.service.advertisement.filter.strategy;

import org.springframework.data.domain.Sort;
import ua.everybuy.service.advertisement.filter.sort.SortStrategy;

public class DateAscendingSortStrategy implements SortStrategy {
    @Override
    public Sort getSortOrder() {
        return Sort.by(Sort.Direction.ASC, "creationDate");
    }
}