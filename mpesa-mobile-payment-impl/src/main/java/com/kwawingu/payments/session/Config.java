package com.kwawingu.payments.session;

import com.kwawingu.payments.session.keys.MpesaApiKey;
import com.kwawingu.payments.session.keys.MpesaPublicKey;

public class Config {
    private final MpesaApiKey mpesaApiKey;
    private final MpesaPublicKey mpesaPublicKey;

    public Config(MpesaApiKey mpesaApiKey, MpesaPublicKey mpesaPublicKey) {
        this.mpesaApiKey = mpesaApiKey;
        this.mpesaPublicKey = mpesaPublicKey;
    }

    public MpesaApiKey getMpesaApiKey() {
        return mpesaApiKey;
    }

    public MpesaPublicKey getMpesaPublicKey() {
        return mpesaPublicKey;
    }
}
