package com.kwawingu.payments.session.keys;

import java.util.Map;

public class MpesaEncryptedApiKey {

    private final String encryptedApiKey;

    public MpesaEncryptedApiKey(String apiKey) {
        this.encryptedApiKey = apiKey;
    }

    public void insertAuthorizationHeader(Map<String, String> headers) {
        headers.put("Authorization", "Bearer " + encryptedApiKey);
    }

    @Override
    public String toString() {
        return "MpesaEncryptedApiKey{" +
                "encryptedApiKey='" + encryptedApiKey + '\'' +
                '}';
    }
}
