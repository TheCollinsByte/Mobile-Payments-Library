package com.kwawingu.payments.session;

public class MpesaKeyProviderFromEnvironment implements MpesaKeyProvider {

    private final Config config;

    public MpesaKeyProviderFromEnvironment(Config config) {
        this.config = config;
    }

    @Override
    public String getApiKey() {
        return System.getenv(config.getMpesaApiKey().getApiKey());
    }

    @Override
    public String getPublicKey() {
        return System.getenv(config.getMpesaPublicKey().getPublicKey());
    }
}