package ua.everybuy.service.advertisement.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.routing.mapper.AdvertisementDocumentMapper;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticsearchReindexService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementDocumentMapper mapper;
    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper objectMapper;

    private static final String INDEX_NAME = "advertisements";

    public String reindexAllAdvertisements() throws IOException {
        createIndexIfNotExists();
        deleteAllDocuments();

        List<Advertisement> allAds = advertisementRepository.findAll();
        BulkRequest bulkRequest = createBulkIndexRequest(allAds);

        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return "Successfully reindexed %d advertisements".formatted(allAds.size());
    }

    private void createIndexIfNotExists() throws IOException {
        if (!indexExists()) {
            CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
            request.source(getIndexMapping(), XContentType.JSON);
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        }
    }

    private boolean indexExists() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(INDEX_NAME);
        return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    private void deleteAllDocuments() throws IOException {
        DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest(INDEX_NAME);
        deleteRequest.setQuery(QueryBuilders.matchAllQuery());
        deleteRequest.setConflicts("proceed");
        restHighLevelClient.deleteByQuery(deleteRequest, RequestOptions.DEFAULT);
    }

    private BulkRequest createBulkIndexRequest(List<Advertisement> advertisements) throws JsonProcessingException {
        BulkRequest bulkRequest = new BulkRequest();

        for (Advertisement ad : advertisements) {
            AdvertisementDocument doc = mapper.mapToDocument(ad);
            doc.setId(ad.getId());

            IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                    .id(String.valueOf(doc.getId()))
                    .source(objectMapper.writeValueAsString(doc), XContentType.JSON);

            bulkRequest.add(indexRequest);
        }

        return bulkRequest;
    }

    private String getIndexMapping() {
        return """
            {
              "settings": {
                "analysis": {
                  "analyzer": {
                    "ukrainian": {
                      "type": "custom",
                      "tokenizer": "standard",
                      "filter": [
                        "lowercase",
                        "ukrainian_stop",
                        "ukrainian_stemmer"
                      ]
                    }
                  },
                  "filter": {
                    "ukrainian_stop": {
                      "type": "stop",
                      "stopwords": "_ukrainian_"
                    },
                    "ukrainian_stemmer": {
                      "type": "stemmer",
                      "language": "ukrainian"
                    }
                  }
                }
              },
              "mappings": {
                "properties": {
                  "id": { "type": "long" },
                  "title": { 
                    "type": "text", 
                    "analyzer": "ukrainian",
                    "fields": {
                      "exact": { "type": "keyword" }
                    }
                  },
                  "description": { "type": "text" },
                  "price": { "type": "double" },
                  "creationDate": { "type": "date" },
                  "updateDate": { "type": "date" },
                  "isEnabled": { "type": "boolean" },
                  "userId": { "type": "long" },
                  "mainPhotoUrl": { "type": "text" },
                  "cityId": { "type": "long" },
                  "topSubCategoryId": { "type": "long" },
                  "lowSubCategoryId": { "type": "long" },
                  "productType": { "type": "keyword" },
                  "section": { "type": "keyword" }
                }
              }
            }
            """;
    }
}
