package ua.everybuy.errorhandling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(HttpClientErrorException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getStatusCode().value(), new MessageResponse(ex.getMessage())));
    }
}
