package ua.everybuy.service.integration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.everybuy.routing.dto.response.ValidResponse;

@Service
@RequiredArgsConstructor
public class AuthValidationService {
   private final ExchangeService exchangeService;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public ValidResponse getValidRequest(HttpServletRequest request) {
        ResponseEntity<ValidResponse> response = exchangeService
                .exchangeGetRequest(request, authServiceUrl, ValidResponse.class);
        return response.getBody();
    }
}
