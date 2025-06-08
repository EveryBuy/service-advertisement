package ua.everybuy.buisnesslogic.service.advertisement.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.everybuy.database.entity.Advertisement;
import ua.everybuy.database.entity.AdvertisementDocument;
import ua.everybuy.database.repository.advertisement.AdvertisementDocumentRepository;
import ua.everybuy.routing.mapper.AdvertisementDocumentMapper;

@Service
@RequiredArgsConstructor
public class AdvertisementIndexService {
    private final AdvertisementDocumentRepository repository;
    private final AdvertisementDocumentMapper mapper;

    public void indexAdvertisement(Advertisement advertisement) {
        AdvertisementDocument doc = mapper.mapToDocument(advertisement);
        doc.setId(advertisement.getId());
        repository.save(doc);
    }

    public void deleteFromIndex(Long id) {
        repository.deleteById(id);
    }
}
