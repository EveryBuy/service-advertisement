package ua.everybuy.service.advertisement.filter.sort;

import org.springframework.data.domain.Sort;

public interface SortStrategy {
    Sort getSortOrder();
}
