package ua.everybuy.service.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WakeUpService {
    private final ExchangeService exchangeService;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private static final String WAKE_UP_ENDPOINT = "/keep-alive";

    @Scheduled(fixedRate = 300_000)
    public void sendEmptyRequestToWakeUpService() {
        String userServiceWakeUpUrl = userServiceUrl + WAKE_UP_ENDPOINT;
        try {
            ResponseEntity<String> response = exchangeService
                    .exchangeGetRequest(userServiceWakeUpUrl, String.class);
            log.info("Send request to user service");
            log.info(response.getBody());

        } catch (HttpStatusCodeException e) {
            log.error("User service returned {} error. Response: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Failed to send request to user service: {}", e.getMessage());
        }
    }

    @Scheduled(fixedRate = 300_000)
    public void sendEmptyRequestToGatewayService() {
        String apiGatewayServiceWakeUpUrl = apiGatewayUrl + WAKE_UP_ENDPOINT;
        try {
            ResponseEntity<String> response = exchangeService
                    .exchangeGetRequest(apiGatewayServiceWakeUpUrl, String.class);
            log.info("Send request to gateway service");
            log.info(response.getBody());

        } catch (HttpStatusCodeException e) {
            log.error("Gateway service returned {} error. Response: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Failed to send request to gateway service: {}", e.getMessage());
        }
    }
}
