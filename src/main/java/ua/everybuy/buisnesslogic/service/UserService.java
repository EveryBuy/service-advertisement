package ua.everybuy.buisnesslogic.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.everybuy.routing.dto.StatusResponse;
import ua.everybuy.routing.dto.StatusResponseUser;
import ua.everybuy.routing.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ExchangeService exchangeService;

    @Value("${user.service.url}")
    private String userServiceUrl;

    //todo need to think about exception handling
    public UserDto getUserInfo(HttpServletRequest request){
        return exchangeService.exchangeRequest(request, userServiceUrl, StatusResponseUser.class).getBody().getData();
    }
}
