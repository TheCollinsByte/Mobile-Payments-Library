package com.kwawingu.payments.Session;

public class MpesaKeyProviderFromEnvironment implements MpesaKeyProvider {
    @Override
    public String getApiKey() {
        return System.getenv("MPESA_API_KEY");
    }

    @Override
    public String getPublicKey() {
        return System.getenv("MPESA_PUBLIC_KEY");
    }
}
