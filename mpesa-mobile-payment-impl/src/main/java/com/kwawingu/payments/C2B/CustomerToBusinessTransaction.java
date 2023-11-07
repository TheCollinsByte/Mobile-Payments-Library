package com.kwawingu.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class CustomerToBusinessTransaction {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransaction.class);

    private final HttpClient httpClient;
    private final ApiEndpoint apiEndpoint;
    private final String encryptedSessionKey;
    private final Payload payload;

    public CustomerToBusinessTransaction(ApiEndpoint apiEndpoint, String encryptedSessionKey, Payload payload) {
        this.apiEndpoint = apiEndpoint;
        this.encryptedSessionKey = encryptedSessionKey;
        this.payload = payload;
        httpClient = HttpClient.newHttpClient();
    }

    public String initiatePayment() throws IOException, InterruptedException {
        String context = apiEndpoint.getUrl(Service.CUSTOMER_TO_BUSINESS);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + encryptedSessionKey);
        headers.put("Origin", "*");

        HttpRequest.Builder requestBuilder = HttpRequest
                .newBuilder()
                .uri(URI.create(context))
                .POST(HttpRequest.BodyPublishers.ofString(payload.toJsonString()));

        headers.forEach(requestBuilder::headers);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static class Builder {
        private ApiEndpoint apiEndpoint;
        private String encryptedSessionKey;
        private Payload payload;

        public Builder setApiEndpoint(ApiEndpoint apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public Builder setEncryptedSessionKey(String encryptedSessionKey) {
            this.encryptedSessionKey = encryptedSessionKey;
            return this;
        }

        public Builder setPayload(Payload payload) {
            this.payload = payload;
            return this;
        }

        public CustomerToBusinessTransaction build() {
            return new CustomerToBusinessTransaction(apiEndpoint, encryptedSessionKey, payload);
        }
    }
}