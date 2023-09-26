package com.kwawingu.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SessionKeyTest {

    private SessionKey mpesaSessionKey;
    private MpesaConfig mpesaConfig;

    @BeforeEach
    public void setUp() {
        String publicKey = System.getenv("MPESA_PUBLIC_KEY");
        String apiKey = System.getenv("MPESA_API_KEY");

        if (publicKey == null || apiKey == null) {
            throw new RuntimeException("Missing environment variables: MPESA_PUBLIC_KEY or MPESA_API_KEY");
        }

        mpesaSessionKey = new SessionKey();
        mpesaConfig = new MpesaConfig(publicKey, apiKey);
    }

    @Test
    public void testClientGetSessionKey() throws IOException {
        String context = "https://openapi.m-pesa.com/sandbox/ipg/v2/vodacomTZN/getSession/";
        String encryptApiKey = mpesaConfig.generateAnEncryptApiKey();
        assertNotNull(encryptApiKey);

        Optional<String> session = mpesaSessionKey.getSessionKey(encryptApiKey, context);

        assertTrue(session.isPresent());
    }
}
