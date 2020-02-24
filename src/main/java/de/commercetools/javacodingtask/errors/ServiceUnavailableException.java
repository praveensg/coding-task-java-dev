package de.commercetools.javacodingtask.errors;

/**
 * The server responded with status code 503.
 */
public class ServiceUnavailableException extends ClientException {
    public ServiceUnavailableException() {
        super("Please try directly again.");
    }
}
