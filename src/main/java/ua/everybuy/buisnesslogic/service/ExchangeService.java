package ua.everybuy.buisnesslogic.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
final class ExchangeService {
    @Value("${user.service.wakeup.url}")
    private String userServiceWakeUpUrl;
    private static final String AUTH_HEADER_PREFIX = "Authorization";
    private final RestTemplate restTemplate;

    public <T> ResponseEntity<T> exchangeRequest(HttpServletRequest request, String url, Class<T> requiredClass) {
        String authHeader = request.getHeader(AUTH_HEADER_PREFIX);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER_PREFIX, authHeader);
        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, requiredClass);
    }

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
