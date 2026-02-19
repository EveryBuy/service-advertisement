package ua.everybuy.database.repository.advertisement.elasticsearch;

import java.io.IOException;
import java.util.List;

public interface ElasticsearchCustomRepository<T, ID> {
    void save(T document) throws IOException;

    void saveAll(List<T> documents) throws IOException;

    void deleteById(ID id) throws IOException;

    void deleteAll() throws IOException;
}
