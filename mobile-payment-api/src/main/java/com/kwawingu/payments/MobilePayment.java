package com.kwawingu.payments;

public interface MobilePayment {
    String generateAnEncryptApiKey();

    String getSessionKey();
}
