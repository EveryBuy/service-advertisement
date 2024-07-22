package ua.everybuy.buisnesslogic.service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WakeUpService {
    private final ExchangeService exchangeService;

    @Value("${user.service.wakeup.url}")
    private String userServiceWakeUpUrl;

    @Scheduled(fixedRate = 150000)
    public void sendEmptyRequestToWakeUpService() {
        ResponseEntity<String> response = exchangeService
                .exchangeGetRequest(userServiceWakeUpUrl, String.class);

        System.out.println("Send request to user service");
        System.out.println(response.getBody());
    }
}
