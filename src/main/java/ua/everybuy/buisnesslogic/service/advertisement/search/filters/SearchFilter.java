package ua.everybuy.buisnesslogic.service.advertisement.search.filters;

import org.elasticsearch.index.query.BoolQueryBuilder;

public interface SearchFilter<T> {
    void apply(BoolQueryBuilder query, T value);
}
