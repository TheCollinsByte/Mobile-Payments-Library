package com.kwawingu.payments.session.keys;

public class MpesaPublicKey {
    private final String publicKey;

    public MpesaPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return System.getenv(publicKey);
    }
}
