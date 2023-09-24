package com.kwawingu.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MpesaConfigTest {
    private MpesaConfig mpesaConfig;
    private HttpClient httpClient;

    @BeforeEach
    public void setUp() {
        String publicKey = System.getenv("MPESA_PUBLIC_KEY");
        String apiKey = System.getenv("MPESA_API_KEY");

        if (publicKey == null || apiKey == null) {
            throw new RuntimeException("Missing environment variables: MPESA_PUBLIC_KEY or MPESA_API_KEY");
        }

        mpesaConfig = new MpesaConfig(publicKey, apiKey);
        httpClient = HttpClient.newHttpClient();
    }

    /**
     * Apart from checking for Null, Needs to implement decryption logic to validate the session key.
     */
    @Test
    public void testGetASessionKey() throws IOException, InterruptedException {
        String encryptedSessionKey = mpesaConfig.generateAnEncryptApiKey();
        assertNotNull(encryptedSessionKey);

        String context = "https://openapi.m-pesa.com/sandbox/ipg/v2/vodacomTZN/getSession/";

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + encryptedSessionKey);
        headers.put("Origin", "*");

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(context))
                .GET();
        headers.forEach(requestBuilder::headers);
        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200 && response.statusCode() != 400) {
            throw new IOException("Unexpected HTTP Code: " + response.statusCode());
        }

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            String[] apiResponse = responseBody.split(",");

            assertEquals("{\"output_ResponseCode\":\"INS-0\"", apiResponse[0]);
            assertEquals("\"output_ResponseDesc\":\"Request processed successfully\"", apiResponse[1]);
        }

        if (response.statusCode() == 400) {
            throw new IOException("Session Creation Failed: " + response.statusCode());
        }
    }

    @Test
    public void testGetClientGetSessionKey() throws IOException {
        String context = "https://openapi.m-pesa.com/sandbox/ipg/v2/vodacomTZN/getSession/";
        String encryptApiKey = mpesaConfig.generateAnEncryptApiKey();
        assertNotNull(encryptApiKey);

        Optional<String> session = mpesaConfig.getSessionKey(encryptApiKey, context);

        assertFalse(session.isPresent());
    }

    @Test
    public void testInvalidPublicKey() {

    }

    @Test
    public void testInvalidApiKey() {

    }
}
