package ua.everybuy.buisnesslogic.service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WakeUpService {
    private final RestTemplate restTemplate;

    @Value("${user.service.wakeup.url}")
    private String userServiceWakeUpUrl;
    @Scheduled(fixedRate = 150000)
    public void sendEmptyRequestToWakeUpService() {
        ResponseEntity<String> exchange = restTemplate.exchange(
                userServiceWakeUpUrl,
                HttpMethod.GET,
                null,
                String.class);

        System.out.println("Send request to user service");
        System.out.println(exchange.getBody());
    }
}
