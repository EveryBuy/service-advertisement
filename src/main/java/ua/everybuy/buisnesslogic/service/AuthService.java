package ua.everybuy.buisnesslogic.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.everybuy.routing.dto.ValidResponse;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String AUTH_HEADER_PREFIX = "Authorization";
    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public void validateRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER_PREFIX);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER_PREFIX, authHeader);
        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(authServiceUrl, HttpMethod.GET, requestEntity, ValidResponse.class);
    }
}

