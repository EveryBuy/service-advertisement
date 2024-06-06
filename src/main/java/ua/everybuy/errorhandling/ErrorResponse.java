package ua.everybuy.errorhandling;

public class ErrorResponse {
    private int status;
    private MessageResponse messageResponse;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.messageResponse = new MessageResponse(message);
    }
}
