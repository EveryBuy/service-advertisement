package ua.everybuy.buisnesslogic.service.advertisement.search.filters.processor;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Component;
import ua.everybuy.buisnesslogic.service.advertisement.search.filters.EnabledFilter;
import ua.everybuy.buisnesslogic.service.advertisement.search.filters.KeywordFilter;

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
