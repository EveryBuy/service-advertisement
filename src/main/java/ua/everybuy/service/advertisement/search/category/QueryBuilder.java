package ua.everybuy.service.advertisement.search.category;

import org.elasticsearch.action.search.SearchRequest;

import java.util.List;

public interface QueryBuilder {
    SearchRequest buildSearchRequest(String keyword, List<String> sections);
}
