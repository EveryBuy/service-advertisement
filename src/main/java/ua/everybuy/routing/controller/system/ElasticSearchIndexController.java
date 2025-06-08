package ua.everybuy.routing.controller.system;

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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.routing.mapper.AdvertisementDocumentMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/search")
public class ElasticSearchIndexController {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementDocumentMapper mapper;
    private final RestHighLevelClient restHighLevelClient;

    private static final String INDEX_NAME = "advertisements"; // Название индекса

    @PostMapping("/reindex")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String reindexAllAdvertisements() {
        try {
            createIndexIfNotExists();

            DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest(INDEX_NAME);
            deleteRequest.setQuery(QueryBuilders.matchAllQuery());
            deleteRequest.setConflicts("proceed");
            restHighLevelClient.deleteByQuery(deleteRequest, RequestOptions.DEFAULT);

            List<Advertisement> allAds = advertisementRepository.findAll();

            BulkRequest bulkRequest = new BulkRequest();

            ObjectMapper objectMapper = new ObjectMapper();

            for (Advertisement ad : allAds) {
                AdvertisementDocument doc = mapper.mapToDocument(ad);
                doc.setId(ad.getId());

                IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                        .id(String.valueOf(doc.getId()))
                        .source(objectMapper.writeValueAsString(doc), XContentType.JSON);

                bulkRequest.add(indexRequest);
            }

            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            return "Successfully reindexed %d advertisements".formatted(allAds.size());

        } catch (Exception e) {
            throw new RuntimeException("Failed to reindex advertisements", e);
        }
    }
    private void createIndexIfNotExists() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(INDEX_NAME);
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (exists) return;

        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);

        String mappingJson = """
        {
          "settings": {
            "analysis": {
              "analyzer": {
                "ukrainian": {
                  "type": "ukrainian"
                }
              }
            }
          },
          "mappings": {
            "properties": {
              "id": { "type": "long" },
              "title": { "type": "text", "analyzer": "ukrainian" },
              "description": { "type": "text", "analyzer": "ukrainian" },
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

        request.source(mappingJson, XContentType.JSON);
        restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }
}
