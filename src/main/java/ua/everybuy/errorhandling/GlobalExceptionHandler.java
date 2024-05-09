package ua.everybuy.errorhandling;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({HttpClientErrorException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(HttpClientErrorException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getStatusCode().value(), new MessageResponse(ex.getMessage())));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(HttpStatus.SC_BAD_REQUEST, new MessageResponse(ex.getMessage())));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(HttpStatus.SC_BAD_REQUEST,
                        new MessageResponse(String.join("; ", errors))));
    }
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        return ResponseEntity
                .status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR,
                        new MessageResponse(ex.getMessage())));
    }
}
