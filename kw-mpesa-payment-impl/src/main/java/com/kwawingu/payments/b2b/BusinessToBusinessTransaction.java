package com.kwawingu.payments.b2b;

import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.Service;
import com.kwawingu.payments.client.MpesaHttpClient;
import com.kwawingu.payments.client.payload.BusinessToBusinessPayload;
import com.kwawingu.payments.client.response.BusinessToBusinessTransactionResponse;
import com.kwawingu.payments.session.keys.MpesaEncryptedSessionKey;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessToBusinessTransaction {

    @SuppressWarnings("UnusedVariable")
    private static final Logger LOG = LoggerFactory.getLogger(BusinessToBusinessTransaction.class);

    private final MpesaHttpClient mpesaHttpClientClient;
    private final ApiEndpoint apiEndpoint;
    private final MpesaEncryptedSessionKey encryptedSessionKey;
    private final BusinessToBusinessPayload payload;

    public BusinessToBusinessTransaction(
            ApiEndpoint apiEndpoint, MpesaEncryptedSessionKey encryptedSessionKey, BusinessToBusinessPayload payload
    ) {
        this.apiEndpoint = apiEndpoint;
        this.encryptedSessionKey = encryptedSessionKey;
        this.payload = payload;
        this.mpesaHttpClientClient = new MpesaHttpClient();
    }

    public String synchronousPayment() throws IOException, InterruptedException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Origin", "*");
        encryptedSessionKey.insertAuthorizationHeader(headers);

        BusinessToBusinessTransactionResponse.SynchronousResponse response =
                mpesaHttpClientClient.businessToBusinessTransactionRequest(
                        headers,
                        HttpRequest.BodyPublishers.ofString(payload.toJsonString()),
                        apiEndpoint.getUrl(Service.BUSINESS_TO_BUSINESS));

        return response.toJson();
    }

    @SuppressWarnings("initialization.field.uninitialized")
    public static class Builder {
        private ApiEndpoint apiEndpoint;
        private MpesaEncryptedSessionKey encryptedSessionKey;
        private BusinessToBusinessPayload payload;

        public Builder setApiEndpoint(ApiEndpoint apiEndpoint) {
            this.apiEndpoint = apiEndpoint;
            return this;
        }

        public Builder setEncryptedSessionKey(MpesaEncryptedSessionKey encryptedSessionKey) {
            this.encryptedSessionKey = encryptedSessionKey;
            return this;
        }

        public Builder setPayload(BusinessToBusinessPayload payload) {
            this.payload = payload;
            return this;
        }

        public BusinessToBusinessTransaction build() {
            Objects.requireNonNull(apiEndpoint, "API End-Point cannot be null");
            Objects.requireNonNull(encryptedSessionKey, "An Encrypted Session Key cannot be null");
            Objects.requireNonNull(payload, "Business To Business Payload cannot be null");

            return new BusinessToBusinessTransaction(apiEndpoint, encryptedSessionKey, payload);
        }
    }
}