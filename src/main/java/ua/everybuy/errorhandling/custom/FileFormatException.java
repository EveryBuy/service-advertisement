package ua.everybuy.errorhandling.custom;

public class FileFormatException extends RuntimeException {
    public FileFormatException (String message) {
        super(message);
    }
}
