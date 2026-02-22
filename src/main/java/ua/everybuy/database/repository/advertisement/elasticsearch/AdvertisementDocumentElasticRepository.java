package ua.everybuy.database.repository.advertisement.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.stereotype.Component;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.errorhandling.custom.ElasticsearchIndexingException;
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

/**
 * Elasticsearch requests cheat sheet:
 * Summary:
 * - IndexRequest → one document insert/update
 * - BulkRequest  → many operations in one call
 * - DeleteRequest → delete one document by id
 * - DeleteByQueryRequest → delete many documents by query
 */


@RequiredArgsConstructor
@Component
public class AdvertisementDocumentElasticRepository implements ElasticsearchCustomRepository<AdvertisementDocument, Long> {
    private static final String INDEX_NAME = "advertisements";
    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    /**
     * 1. IndexRequest
     * - Used to create or update a single document in an index.
     * - You can specify the document id and the JSON source.
     * If a document with the same id exists, it will be overwritten.
     */
    @Override
    public void save(AdvertisementDocument document) {
        try {
            IndexRequest request = new IndexRequest(INDEX_NAME)
                    .id(String.valueOf(document.getId()))
                    .source(objectMapper.writeValueAsString(document), XContentType.JSON);

            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticsearchIndexingException(
                    "Failed to index document with id: " + document.getId());
        }
    }

    /**
     * 2. BulkRequest
     * - Used to execute multiple operations (index, update, delete) in a single request.
     * - Useful for batch indexing to improve performance.
     */
    @Override
    public void saveAll(List<AdvertisementDocument> documents) {
        try {
            BulkRequest bulkRequest = new BulkRequest();

            for (AdvertisementDocument doc : documents) {
                IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                        .id(String.valueOf(doc.getId()))
                        .source(objectMapper.writeValueAsString(doc), XContentType.JSON);

                bulkRequest.add(indexRequest);
            }

            if (!bulkRequest.requests().isEmpty()) {
                BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);

                if (response.hasFailures()) {
                    throw new ElasticsearchIndexingException("Bulk indexing failed: "
                            + response.buildFailureMessage());
                }
            }
        } catch (IOException e) {
            throw new ElasticsearchIndexingException(
                    "Bulk indexing failed: " + e.getMessage(), e);
        }
    }

    /**
     * 3. DeleteRequest
     * - Deletes a single document by its id from an index.
     */
    @Override
    public void deleteById(Long id) {
        try {
            DeleteRequest request = new DeleteRequest(INDEX_NAME, String.valueOf(id));
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

            if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                throw new ElasticsearchIndexingException(
                        "Document not found with id: " + id);
            }
        } catch (IOException e) {
            throw new ElasticsearchIndexingException("Failed to index document with id: " + id, e);
        }
    }

    /**
     * 4. DeleteByQueryRequest
     * - Deletes all documents that match a query from an index.
     * - Useful for bulk deletes without removing the entire index.
     */
    @Override
    public void deleteAll() {
        try {
            DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest(INDEX_NAME);
            deleteRequest.setQuery(QueryBuilders.matchAllQuery());
            deleteRequest.setConflicts("proceed");

            client.deleteByQuery(deleteRequest, RequestOptions.DEFAULT);

        } catch (IOException e) {
            throw new ElasticsearchIndexingException("Failed to delete documents", e);
        }
    }
}
