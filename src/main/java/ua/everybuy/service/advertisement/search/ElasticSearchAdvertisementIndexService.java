package ua.everybuy.service.advertisement.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.routing.mapper.AdvertisementDocumentMapper;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ElasticSearchAdvertisementIndexService {
    private final RestHighLevelClient client;
    private final AdvertisementDocumentMapper mapper;
    private static final String INDEX_NAME = "advertisements";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void indexAdvertisement(Advertisement advertisement) {
        try {
            AdvertisementDocument doc = mapper.mapToDocument(advertisement);
            doc.setId(advertisement.getId());

            IndexRequest request = new IndexRequest(INDEX_NAME)
                    .id(String.valueOf(doc.getId()))
                    .source(objectMapper.writeValueAsString(doc), XContentType.JSON);

            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Failed to index advertisement", e);
        }
    }

    public void deleteFromIndex(Long id) {
        try {
            DeleteRequest request = new DeleteRequest(INDEX_NAME, String.valueOf(id));
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete advertisement from index", e);
        }
    }
}
