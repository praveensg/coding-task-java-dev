package de.commercetools.javacodingtask.errors;

/**
 * Base class for all exceptions {@link de.commercetools.javacodingtask.client.Client} should throw.
 */
public class ClientException extends RuntimeException {

    public ClientException(final Exception e) {
        super(e);
    }

    public ClientException(final String message) {
        super(message);
    }
}
