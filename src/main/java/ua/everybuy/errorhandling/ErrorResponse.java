package ua.everybuy.errorhandling;

public record ErrorResponse(int status, MessageResponse error) {
}
