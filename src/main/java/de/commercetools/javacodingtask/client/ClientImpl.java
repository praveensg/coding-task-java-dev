package de.commercetools.javacodingtask.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.commercetools.javacodingtask.errors.ClientException;
import de.commercetools.javacodingtask.errors.ServiceUnavailableException;
import de.commercetools.javacodingtask.models.Customer;
import de.commercetools.javacodingtask.models.Order;
import io.sphere.sdk.models.Base;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

final class ClientImpl extends Base implements Client {
    private static final String BASE_URL = "http://limitless-escarpment-3158.herokuapp.com/";
    private static final ObjectMapper objectMapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String ORDERS = "/orders";
    private static final String CUSTOMERS = "/customers";

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final String applicantEmail;

    public ClientImpl(final String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    @Override
    public ImportResults importOrders(final List<Order> orders) {
        return importEntity(orders, ORDERS);
    }

    @Override
    public ImportResults importCustomer(final List<Customer> customers) {
        return importEntity(customers, CUSTOMERS);
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(httpClient);
    }

    private <T> String toJsonString(final T object) {
        try {
            return objectMapper.writer().writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new ClientException(e);
        }
    }

    private <T> ImportResults importEntity(final List<T> elements, final String path) {
        return executeRequest("POST", BASE_URL + path, toJsonString(elements), ImportResults.class);
    }

    private <T> T executeRequest(final String method, final String uri, final String body, final Class<T> clazz) {
        try {
            final RequestBuilder requestBuilder = RequestBuilder
                    .create(method)
                    .setUri(uri)
                    .setHeader("User-Agent", applicantEmail);
            if (body != null) {
                requestBuilder.setEntity(new StringEntity(body));
            }
            final HttpUriRequest httpUriRequest = requestBuilder.build();
            return httpClient.execute(httpUriRequest, new InternalResponseHandler<T>(clazz));
        } catch (final ClientException e) {
            throw e;
        } catch (final Exception e) {
            throw new ClientException(e);
        }
    }

    private static class InternalResponseHandler<T> implements ResponseHandler<T> {
        final Class<T> valueType;

        public InternalResponseHandler(final Class<T> valueType) {
            this.valueType = valueType;
        }

        @Override
        public T handleResponse(final HttpResponse httpResponse) throws IOException {
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 503) {
                throw new ServiceUnavailableException();
            }
            return objectMapper.readValue(httpResponse.getEntity().getContent(), valueType);
        }
    }
}
