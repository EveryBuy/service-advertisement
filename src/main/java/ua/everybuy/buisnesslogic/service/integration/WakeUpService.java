package ua.everybuy.buisnesslogic.service.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WakeUpService {
    private final ExchangeService exchangeService;
    @Value("${user.service.url}")
    private String userServiceUrl;
    private static final String WAKE_UP_ENDPOINT = "/keep-alive";

    @Scheduled(fixedRate = 200000)
    public void sendEmptyRequestToWakeUpService() {
        String userServiceWakeUpUrl = userServiceUrl + WAKE_UP_ENDPOINT;
        ResponseEntity<String> response = exchangeService
                .exchangeGetRequest(userServiceWakeUpUrl, String.class);

        log.info("Send request to user service");
        log.info(response.getBody());
    }
}
