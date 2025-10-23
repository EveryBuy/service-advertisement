package ua.everybuy.service.integration.chat;

import ua.everybuy.routing.dto.response.AdvertisementInfoForChatService;

public interface AdvertisementSender {
    void sendAdvertisementChange(AdvertisementInfoForChatService advertisementInfo);
}
