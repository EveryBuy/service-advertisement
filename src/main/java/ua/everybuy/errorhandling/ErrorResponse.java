package ua.everybuy.errorhandling;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int status;
    private MessageResponse messageResponse;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.messageResponse = new MessageResponse(message);
    }
}
