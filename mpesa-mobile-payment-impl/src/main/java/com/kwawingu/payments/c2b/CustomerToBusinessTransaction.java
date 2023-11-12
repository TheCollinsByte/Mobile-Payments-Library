/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.c2b;

import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.Service;
import com.kwawingu.payments.client.MpesaHttpClient;
import com.kwawingu.payments.client.payload.Payload;
import com.kwawingu.payments.session.keys.MpesaEncryptedSessionKey;
import java.io.IOException;
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

  private final MpesaHttpClient mpesaHttpClientClient;
  private final ApiEndpoint apiEndpoint;
  private final MpesaEncryptedSessionKey encryptedSessionKey;
  private final Payload payload;

  public CustomerToBusinessTransaction(
      ApiEndpoint apiEndpoint, MpesaEncryptedSessionKey encryptedSessionKey, Payload payload) {
    this.apiEndpoint = apiEndpoint;
    this.encryptedSessionKey = encryptedSessionKey;
    this.payload = payload;
    mpesaHttpClientClient = new MpesaHttpClient();
  }

  public String synchronousPayment() throws IOException, InterruptedException {

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Origin", "*");
    encryptedSessionKey.insertAuthorizationHeader(headers);

    HttpResponse<String> response = mpesaHttpClientClient.postRequest(headers, HttpRequest.BodyPublishers.ofString(payload.toJsonString()), apiEndpoint.getUrl(Service.CUSTOMER_TO_BUSINESS));
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
