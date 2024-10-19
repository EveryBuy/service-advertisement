package ua.everybuy.buisnesslogic.strategy.sort;

import org.springframework.data.domain.Sort;

public interface SortStrategy {
    Sort getSortOrder();
}
