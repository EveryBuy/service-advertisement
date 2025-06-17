package ua.everybuy.service.advertisement.filter.sort;

import org.springframework.data.domain.Sort;

public class PriceDescendingSortStrategy implements SortStrategy {
    @Override
    public Sort getSortOrder() {
        return Sort.by(Sort.Direction.DESC, "price");
    }
}
