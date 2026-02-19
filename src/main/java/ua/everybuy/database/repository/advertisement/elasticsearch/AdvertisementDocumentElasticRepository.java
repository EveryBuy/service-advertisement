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
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AdvertisementDocumentElasticRepository implements ElasticsearchCustomRepository<AdvertisementDocument, Long> {
    private static final String INDEX_NAME = "advertisements";
    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    @Override
    public void save(AdvertisementDocument document) throws IOException {
        IndexRequest request = new IndexRequest(INDEX_NAME)
                .id(String.valueOf(document.getId()))
                .source(objectMapper.writeValueAsString(document), XContentType.JSON);

        client.index(request, RequestOptions.DEFAULT);
    }

    @Override
    public void saveAll(List<AdvertisementDocument> documents) throws IOException {
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
                throw new IOException("Bulk indexing failed: " + response.buildFailureMessage());
            }
        }
    }

    @Override
    public void deleteById(Long id) throws IOException {
        DeleteRequest request = new DeleteRequest(INDEX_NAME, String.valueOf(id));

        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

        if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
            throw new IOException("Document not found with id: " + id);
        }
    }

    @Override
    public void deleteAll() throws IOException {
        DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest(INDEX_NAME);
        deleteRequest.setQuery(QueryBuilders.matchAllQuery());
        deleteRequest.setConflicts("proceed");

        client.deleteByQuery(deleteRequest, RequestOptions.DEFAULT);
    }
}
