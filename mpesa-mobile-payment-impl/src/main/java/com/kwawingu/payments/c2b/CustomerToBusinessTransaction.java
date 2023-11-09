/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.c2b;

import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.Service;
import com.kwawingu.payments.session.keys.MpesaEncryptedSessionKey;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerToBusinessTransaction {
  @SuppressWarnings("UnusedVariable")
  private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransaction.class);

  private final HttpClient httpClient;
  private final ApiEndpoint apiEndpoint;
  private final MpesaEncryptedSessionKey encryptedSessionKey;
  private final Payload payload;

  public CustomerToBusinessTransaction(
      ApiEndpoint apiEndpoint, MpesaEncryptedSessionKey encryptedSessionKey, Payload payload) {
    this.apiEndpoint = apiEndpoint;
    this.encryptedSessionKey = encryptedSessionKey;
    this.payload = payload;
    httpClient = HttpClient.newHttpClient();
  }

  public String synchronousPayment() throws IOException, InterruptedException {
    URI contextUrl = null;
    try {
      contextUrl = apiEndpoint.getUrl(Service.CUSTOMER_TO_BUSINESS);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Origin", "*");
    encryptedSessionKey.insertAuthorizationHeader(headers);

    HttpRequest.Builder requestBuilder =
        HttpRequest.newBuilder()
            .uri(contextUrl)
            .POST(HttpRequest.BodyPublishers.ofString(payload.toJsonString()));

    headers.forEach(requestBuilder::headers);
    HttpRequest request = requestBuilder.build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    return response.body();
  }

  @SuppressWarnings("initialization.field.uninitialized")
  public static class Builder {
    private ApiEndpoint apiEndpoint;
    private MpesaEncryptedSessionKey encryptedSessionKey;
    private Payload payload;

    public Builder setApiEndpoint(ApiEndpoint apiEndpoint) {
      this.apiEndpoint = apiEndpoint;
      return this;
    }

    public Builder setEncryptedSessionKey(MpesaEncryptedSessionKey encryptedSessionKey) {
      this.encryptedSessionKey = encryptedSessionKey;
      return this;
    }

    public Builder setPayload(Payload payload) {
      this.payload = payload;
      return this;
    }

    public CustomerToBusinessTransaction build() {
      Objects.requireNonNull(apiEndpoint, "API End-Point cannot be null");
      Objects.requireNonNull(encryptedSessionKey, "An Encrypted Session Key cannot be null");
      Objects.requireNonNull(payload, "Payload cannot be null");

      return new CustomerToBusinessTransaction(apiEndpoint, encryptedSessionKey, payload);
    }
  }
}
