package ua.everybuy.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecurityValidationService {
    @Value("${service.password.value}")
    private String servicePassword;

    public static final String SERVICE_PASSWORD_HEADER = "Service-Password";
    public static final String PASSWORD_MISMATCH_ERROR = "Password does not match";

    public void validatePassword(HttpServletRequest request) {
        String servicePassword = request.getHeader(SERVICE_PASSWORD_HEADER);
        if (servicePassword == null || !servicePassword.equals(this.servicePassword)) {
            throw new IllegalArgumentException(PASSWORD_MISMATCH_ERROR);
        }
    }
}
