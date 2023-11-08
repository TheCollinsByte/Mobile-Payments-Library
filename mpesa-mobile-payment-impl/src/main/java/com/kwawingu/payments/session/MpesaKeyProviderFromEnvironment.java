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
        return new MpesaApiKey(System.getenv(config.getApiKeyEnvName()));
    }

    @Override
    public MpesaPublicKey getPublicKey() {
        return new MpesaPublicKey(System.getenv(config.getPublicKeyEnvName()));
    }
}
