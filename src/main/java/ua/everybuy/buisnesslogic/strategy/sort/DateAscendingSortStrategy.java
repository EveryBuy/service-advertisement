package ua.everybuy.buisnesslogic.strategy.sort;

import org.springframework.data.domain.Sort;

public class DateAscendingSortStrategy implements SortStrategy {
    @Override
    public Sort getSortOrder() {
        return Sort.by(Sort.Direction.ASC, "creationDate");
    }
}