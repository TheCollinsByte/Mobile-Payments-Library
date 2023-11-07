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

    public CustomerToBusinessTransaction(ApiEndpoint apiEndpoint, String encryptedSessionKey) {
        this.apiEndpoint = apiEndpoint;
        this.encryptedSessionKey = encryptedSessionKey;
        httpClient = HttpClient.newHttpClient();
    }

    public String initiatePayment() throws IOException, InterruptedException {
        String context = apiEndpoint.getUrl(Service.CUSTOMER_TO_BUSINESS);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + encryptedSessionKey);
        headers.put("Origin", "*");

        String jsonPayload = "{\n" +
                "    \"input_Amount\": \"10.00\",\n" +
                "    \"input_CustomerMSISDN\": \"000000000001\",\n" +
                "    \"input_Country\": \"TZN\",\n" +
                "    \"input_Currency\": \"TZS\",\n" +
                "    \"input_ServiceProviderCode\": \"000000\",\n" +
                "    \"input_TransactionReference\": \"T1234C\",\n" +
                "    \"input_ThirdPartyConversationID\": \"asv02e5958774f783d0d689761\",\n" +
                "    \"input_PurchasedItemsDesc\": \"Library\"\n" +
                "}";

        HttpRequest.Builder requestBuilder = HttpRequest
                .newBuilder()
                .uri(URI.create(context))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload));

        headers.forEach(requestBuilder::headers);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

}
