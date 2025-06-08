package ua.everybuy.routing.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.database.repository.advertisement.AdvertisementDocumentRepository;
import ua.everybuy.database.repository.advertisement.AdvertisementRepository;
import ua.everybuy.routing.mapper.AdvertisementDocumentMapper;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/search")
public class ElasticSearchIndexController {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementDocumentMapper mapper;
    private final AdvertisementDocumentRepository advertisementDocumentRepository;

    @PostMapping("/reindex")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String reindexAllAdvertisements() {

        try {
            advertisementDocumentRepository.deleteAll();

            List<Advertisement> allAds = advertisementRepository.findAll();

            List<AdvertisementDocument> docs = allAds.stream()
                    .map(ad -> {
                        AdvertisementDocument doc = mapper.mapToDocument(ad);
                        doc.setId(ad.getId());
                        return doc;
                    })
                    .toList();

            advertisementDocumentRepository.saveAll(docs);

            return "Successfully reindexed %d advertisements".formatted(docs.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to reindex advertisements");
        }
    }
}
