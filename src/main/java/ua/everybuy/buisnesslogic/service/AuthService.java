package ua.everybuy.buisnesslogic.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.everybuy.routing.dto.request.ValidRequest;

@Service
@RequiredArgsConstructor
public class AuthService {
   private final ExchangeService exchangeService;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public<T> void validRequest(HttpServletRequest request){
        exchangeService.exchangeRequest(request, authServiceUrl, ValidRequest.class);
    }


}

