package ua.everybuy.security;

import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpStatusCodeException;
import ua.everybuy.buisnesslogic.service.AuthService;
import ua.everybuy.errorhandling.ErrorResponse;
import ua.everybuy.errorhandling.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private static final Set<String> EXCLUDED_PATHS = Set.of("/ad/category/list", "/ad/category/list/ukr", "/ad/city/list");

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            authService.validateRequest(request);
            filterChain.doFilter(request, response);

        } catch (HttpClientErrorException ex) {
            handleClientError(response, ex);
        } catch (HttpStatusCodeException ex) {
            handleServerError(response, ex);
        }
    }

    public boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDED_PATHS.contains(request.getRequestURI());
    }

    private void handleClientError(HttpServletResponse response, HttpClientErrorException e) throws IOException {
        extractErrorMessage(response, e, e.getStatusCode().value());
    }

    private void handleServerError(HttpServletResponse response, HttpStatusCodeException e) throws IOException {
        extractErrorMessage(response, e, e.getStatusCode().value());
    }

    private void extractErrorMessage(HttpServletResponse response, Exception e, int statusCode) throws IOException {
        String message = e.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(statusCode, new MessageResponse(message));
        String json = objectMapper.writeValueAsString(errorResponse);
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
    }
}
