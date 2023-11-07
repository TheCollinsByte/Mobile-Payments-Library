/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptApiKeyTest {

  private static final Logger LOG = LoggerFactory.getLogger(EncryptApiKeyTest.class);

  private EncryptApiKey encryptApiKey;
  private HttpClient httpClient;
  private ApiEndpoint apiEndpoint;

  @BeforeEach
  public void setUp() {
    String publicKey = System.getenv("MPESA_PUBLIC_KEY");
    String apiKey = System.getenv("MPESA_API_KEY");

    if (publicKey == null || apiKey == null) {
      throw new RuntimeException(
          "Missing environment variables: MPESA_PUBLIC_KEY or MPESA_API_KEY");
    }

    encryptApiKey = new EncryptApiKey(publicKey, apiKey);
    httpClient = HttpClient.newHttpClient();
    apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
  }

  /**
   * Apart from checking for Null, Needs to implement decryption logic to validate the session key.
   */
  @Test
  public void testGetASessionKey() throws IOException, InterruptedException {
    String encryptedSessionKey = encryptApiKey.generateAnEncryptApiKey();
    assertNotNull(encryptedSessionKey);

    String context = apiEndpoint.getUrl(Service.GET_SESSION);
    LOG.info(context);

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Authorization", "Bearer " + encryptedSessionKey);
    headers.put("Origin", "*");

    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(context)).GET();
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
  public void testInvalidPublicKey() {}

  @Test
  public void testInvalidApiKey() {}
}
