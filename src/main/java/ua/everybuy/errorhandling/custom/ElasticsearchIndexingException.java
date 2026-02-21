package ua.everybuy.errorhandling.custom;

public class ElasticsearchIndexingException extends RuntimeException {
    public ElasticsearchIndexingException(String message) {
        super(message);
    }
    public ElasticsearchIndexingException(String message, Throwable cause) {
        super(message, cause);
    }
}
