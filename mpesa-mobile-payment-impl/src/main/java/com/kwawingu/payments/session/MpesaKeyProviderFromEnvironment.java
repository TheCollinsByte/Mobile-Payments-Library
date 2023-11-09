package com.kwawingu.payments.session;

import com.kwawingu.payments.session.keys.MpesaApiKey;
import com.kwawingu.payments.session.keys.MpesaPublicKey;

public class MpesaKeyProviderFromEnvironment implements MpesaKeyProvider {

    private final Config config;

    public MpesaKeyProviderFromEnvironment(Config config) {
        this.config = config;
    }

    @Override
    public MpesaApiKey getApiKey() {
        String apiKey = System.getenv(config.getApiKeyEnvName());
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("Your Did not provide the API Key in " + config.getApiKeyEnvName());
        }
        return new MpesaApiKey(apiKey);
    }

    @Override
    public MpesaPublicKey getPublicKey() {
        String publicKey = System.getenv(config.getPublicKeyEnvName());
        if (publicKey == null || publicKey.trim().isEmpty()) {
            throw new IllegalStateException("You did not provide the API Key in " + config.getPublicKeyEnvName());
        }
        return new MpesaPublicKey(publicKey);
    }
}
