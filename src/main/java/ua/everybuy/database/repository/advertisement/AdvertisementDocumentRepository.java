package ua.everybuy.database.repository.advertisement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ua.everybuy.database.entity.AdvertisementDocument;

public interface AdvertisementDocumentRepository
        extends ElasticsearchRepository<AdvertisementDocument, Long> {
    @Query("{\"match\": {\"title\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    Page<AdvertisementDocument> findByTitleFuzzy(String title, Pageable pageable);
}
