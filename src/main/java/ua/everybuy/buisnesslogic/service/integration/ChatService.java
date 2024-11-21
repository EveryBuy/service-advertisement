package ua.everybuy.buisnesslogic.service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.everybuy.routing.dto.response.AdvertisementInfoForChatService;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final String CHANGE_INFO_ENDPOINT = "/advertisement/change";
    private final RestTemplate restTemplate;
    @Value("${chat.service.url}")
    private String chatServiceUrl;

    public void sendInfoAboutChange(AdvertisementInfoForChatService advertisementInfo) {
        String url = chatServiceUrl + CHANGE_INFO_ENDPOINT;
        HttpEntity<AdvertisementInfoForChatService> requestEntity = new HttpEntity<>(advertisementInfo);

            restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    AdvertisementInfoForChatService.class);
    }

}
