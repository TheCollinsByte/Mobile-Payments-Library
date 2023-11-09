/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.session.keys;

import java.util.Map;

public class MpesaEncryptedSessionKey {

  private final String encryptedSessionKey;

  public MpesaEncryptedSessionKey(String encryptedSessionKey) {
    this.encryptedSessionKey = encryptedSessionKey;
  }

  public void insertAuthorizationHeader(Map<String, String> header) {
    header.put("Authorization", "Bearer " + encryptedSessionKey);
  }
}
