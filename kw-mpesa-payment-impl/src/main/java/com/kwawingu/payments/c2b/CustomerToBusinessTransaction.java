/*
 * Copyright 2021-2024 KwaWingu.
 */
package com.kwawingu.payments.c2b;

import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.Service;
import com.kwawingu.payments.client.MpesaHttpClient;
import com.kwawingu.payments.client.payload.CustomerToBusinessPayload;
import com.kwawingu.payments.client.response.CustomerToBusinessTransactionResponse;
import com.kwawingu.payments.session.keys.MpesaEncryptedSessionKey;
import java.io.IOException;
import java.net.http.HttpRequest;
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
  private final CustomerToBusinessPayload customerToBusinessPayload;

  public CustomerToBusinessTransaction(
      ApiEndpoint apiEndpoint, MpesaEncryptedSessionKey encryptedSessionKey, CustomerToBusinessPayload customerToBusinessPayload) {
    this.apiEndpoint = apiEndpoint;
    this.encryptedSessionKey = encryptedSessionKey;
    this.customerToBusinessPayload = customerToBusinessPayload;
    mpesaHttpClientClient = new MpesaHttpClient();
  }

  public String synchronousPayment() throws IOException, InterruptedException {

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Origin", "*");
    encryptedSessionKey.insertAuthorizationHeader(headers);

    CustomerToBusinessTransactionResponse.SynchronousResponses response =
        mpesaHttpClientClient.customerToBusinessTransactionRequest(
            headers,
            HttpRequest.BodyPublishers.ofString(customerToBusinessPayload.toJsonString()),
            apiEndpoint.getUrl(Service.CUSTOMER_TO_BUSINESS));
    return response.toJson();
  }

  @SuppressWarnings("initialization.field.uninitialized")
  public static class Builder {
    private ApiEndpoint apiEndpoint;
    private MpesaEncryptedSessionKey encryptedSessionKey;
    private CustomerToBusinessPayload customerToBusinessPayload;

    public Builder setApiEndpoint(ApiEndpoint apiEndpoint) {
      this.apiEndpoint = apiEndpoint;
      return this;
    }

    public Builder setEncryptedSessionKey(MpesaEncryptedSessionKey encryptedSessionKey) {
      this.encryptedSessionKey = encryptedSessionKey;
      return this;
    }

    public Builder setPayload(CustomerToBusinessPayload customerToBusinessPayload) {
      this.customerToBusinessPayload = customerToBusinessPayload;
      return this;
    }

    public CustomerToBusinessTransaction build() {
      Objects.requireNonNull(apiEndpoint, "API End-Point cannot be null");
      Objects.requireNonNull(encryptedSessionKey, "An Encrypted Session Key cannot be null");
      Objects.requireNonNull(customerToBusinessPayload, "CustomerToBusinessPayload cannot be null");

      return new CustomerToBusinessTransaction(apiEndpoint, encryptedSessionKey, customerToBusinessPayload);
    }
  }
}
