package com.kwawingu.payments;

import java.io.IOException;

public interface MobilePayment {
    String generateAnEncryptApiKey();

    String getSessionKey(String encryptedApiKey, String context) throws IOException;
}
