package ua.everybuy.service.integration.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.everybuy.routing.dto.response.AdvertisementInfoForChatService;

@Service
@RequiredArgsConstructor
@Primary
public class AdvertisementChatServiceSender implements AdvertisementSender {
    private static final String CHANGE_INFO_ENDPOINT = "/advertisement/change";
    @Value("${chat.service.url}")
    private String chatServiceUrl;
    private final RestTemplate restTemplate;

    @Override
    public void sendAdvertisementChange(AdvertisementInfoForChatService advertisementInfo) {
        sendInfoAboutChange(advertisementInfo);
    }

    private void sendInfoAboutChange(AdvertisementInfoForChatService advertisementInfo) {
        String url = chatServiceUrl + CHANGE_INFO_ENDPOINT;
        HttpEntity<AdvertisementInfoForChatService> requestEntity = new HttpEntity<>(advertisementInfo);

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                AdvertisementInfoForChatService.class);
        System.out.println("Send info about change");
    }
}
