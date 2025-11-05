package ua.everybuy.service.integration.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ua.everybuy.routing.dto.response.AdvertisementInfoForChatService;

@Service
@RequiredArgsConstructor
@Primary
public class AdvertisementKafkaSender implements AdvertisementSender {
    private final KafkaTemplate<String, AdvertisementInfoForChatService> kafkaTemplate;
    private static final String TOPIC = "chat-ad-updates";

    @Override
    public void sendAdvertisementChange(AdvertisementInfoForChatService advertisementInfo) {
        kafkaTemplate.send(TOPIC, advertisementInfo.getId().toString(), advertisementInfo);
    }
}
