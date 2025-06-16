package ua.everybuy.service.advertisement.search.filters.processors;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Component;
import ua.everybuy.service.advertisement.search.filters.EnabledFilter;
import ua.everybuy.service.advertisement.search.filters.KeywordFilter;

@RequiredArgsConstructor
@Component
public class FilterAdvertisementCategoryProcessor {
    private final EnabledFilter enabledFilter;
    private final KeywordFilter keywordFilter;

    public void process(BoolQueryBuilder query, String keyword) {
        enabledFilter.apply(query, new Object());
        keywordFilter.apply(query,keyword);

    }
}
