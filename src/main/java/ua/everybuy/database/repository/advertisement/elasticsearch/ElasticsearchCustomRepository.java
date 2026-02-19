package ua.everybuy.database.repository.advertisement.elasticsearch;

import java.io.IOException;
import java.util.List;

/**
 * Custom Elasticsearch repository for AdvertisementDocument.
 * <p>
 * Provides basic CRUD-like operations using RestHighLevelClient.
 * Internally uses IndexRequest, BulkRequest and DeleteByQueryRequest
 * to interact with the Elasticsearch index.
 * <p>
 * Index structure:
 * index  → advertisements
 * document → AdvertisementDocument
 * <p>
 * table = index
 * document = row
 * <p>
 * This repository gives full control over low-level Elasticsearch operations
 * without using Spring Data Elasticsearch.
 */

public interface ElasticsearchCustomRepository<T, ID> {
    void save(T document) throws IOException;

    void saveAll(List<T> documents) throws IOException;

    void deleteById(ID id) throws IOException;

    void deleteAll() throws IOException;
}
