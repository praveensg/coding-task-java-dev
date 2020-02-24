package de.commercetools.javacodingtask.client;

import io.sphere.sdk.models.Base;

/**
 * Creates a client.
 */
public final class ClientFactory extends Base {
    private ClientFactory() {
        //only static factory method should be used
    }

    /**
     * Creates a client which uses applicantEmail as user agent.
     * @param applicantEmail the email of YOU
     * @return a new client
     */
    public static Client create(final String applicantEmail) {
        return new ClientImpl(applicantEmail);
    }
}
