package ua.everybuy.routing.controller.system;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.everybuy.service.advertisement.search.AdvertisementReindexingService;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/search")
public class ElasticSearchIndexController {
    private final AdvertisementReindexingService reindexService;

    @PostMapping("/reindex")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String reindexAllAdvertisements() throws IOException {
        return reindexService.reindexAllAdvertisements();
    }
}
