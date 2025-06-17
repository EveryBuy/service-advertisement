package ua.everybuy.service.advertisement.filter.sort;

import org.springframework.data.domain.Sort;

public class DefaultSortStrategy implements SortStrategy {
    @Override
    public Sort getSortOrder() {
        return Sort.unsorted();
    }
}
