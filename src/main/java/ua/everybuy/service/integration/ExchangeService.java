package ua.everybuy.service.integration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
final class ExchangeService {
    private static final String AUTH_HEADER_PREFIX = "Authorization";
    private final RestTemplate restTemplate;

    public <T> ResponseEntity<T> exchangeGetRequest(HttpServletRequest request, String url, Class<T> requiredClass) {
        String authHeader = request.getHeader(AUTH_HEADER_PREFIX);
        HttpHeaders headers = new HttpHeaders();
        if (authHeader != null) {
            headers.add(AUTH_HEADER_PREFIX, authHeader);
        }
        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestEntity, requiredClass);
    }

    public <T> ResponseEntity<T> exchangeGetRequest(String url, Class<T> requiredClass) {
        return restTemplate.exchange(url, HttpMethod.GET, null, requiredClass);
    }
}
