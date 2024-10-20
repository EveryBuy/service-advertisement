package ua.everybuy.buisnesslogic.strategy.sort;

import org.springframework.data.domain.Sort;

public class DefaultSortStrategy implements SortStrategy {
    @Override
    public Sort getSortOrder() {
        return Sort.unsorted();
    }
}
