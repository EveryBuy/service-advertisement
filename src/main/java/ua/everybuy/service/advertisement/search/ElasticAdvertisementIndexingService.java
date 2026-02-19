package ua.everybuy.service.advertisement.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.database.repository.advertisement.elasticsearch.AdvertisementDocumentElasticRepository;
import ua.everybuy.routing.mapper.AdvertisementDocumentMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ElasticAdvertisementIndexingService implements AdvertisementIndexingService {
    private static final String INDEX_NAME = "advertisements";
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementDocumentElasticRepository adDocElasticRepository;
    private final AdvertisementDocumentMapper mapper;
    private final RestHighLevelClient restHighLevelClient;

    @Override
    public void indexAdvertisement(Advertisement advertisement) {
        try {
            AdvertisementDocument doc = mapper.mapToDocument(advertisement);
            adDocElasticRepository.save(doc);
            log.info("Indexing advertisement {}", advertisement.getId());
        } catch (IOException e) {
            throw new RuntimeException("Failed to index advertisement", e);
        }
    }

    @Override
    public void deleteFromIndex(Long id) {
        try {
            adDocElasticRepository.deleteById(id);
            log.info("Deleting advertisement {}", id);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete advertisement from index", e);
        }
    }

    @Override
    public String reindexAllAdvertisements() throws IOException {
        createIndexIfNotExists();
        adDocElasticRepository.deleteAll();

        List<Advertisement> allAds = advertisementRepository.findAll();

        List <AdvertisementDocument> documents = allAds.stream()
                .map(mapper::mapToDocument)
                .collect(Collectors.toList());

        adDocElasticRepository.saveAll(documents);
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
                      "price": { "type": "long" }, 
                      "creationDate": { "type": "date" },
                      "updateDate": { "type": "date" },
                      "isEnabled": { "type": "boolean" },
                      "isNegotiable": { "type": "boolean" },
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
