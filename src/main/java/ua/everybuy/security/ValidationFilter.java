package ua.everybuy.security;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
import ua.everybuy.routing.dto.ValidResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final AuthService authService;

    private static final Set<String> EXCLUDED_PATHS = Set.of("/ad/category", "/ad/category/ukr",
            "/ad/subcategory", "/ad/city");

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
            ValidResponse validResponse = authService.getValidRequest(request);
            if (validResponse != null) {
                String userId = String.valueOf(validResponse.getData().getUserId());
                List<SimpleGrantedAuthority> grantedAuthorities = validResponse.getData().getRoles()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId, null, grantedAuthorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        } catch (HttpClientErrorException ex) {
            handleClientError(response, ex);
            return;
        } catch (HttpStatusCodeException ex) {
            handleServerError(response, ex);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
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
