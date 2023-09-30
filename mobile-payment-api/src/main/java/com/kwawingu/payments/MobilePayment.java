/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import java.io.IOException;
import java.util.Optional;

public interface MobilePayment {
  String generateAnEncryptApiKey();

  Optional<String> getSessionKey(String encryptedApiKey, String context) throws IOException;
}
