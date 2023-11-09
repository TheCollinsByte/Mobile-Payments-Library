/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.session.keys;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class MpesaPublicKey {
  private final String publicKey;

  public MpesaPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public PublicKey toRsaPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
    return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
  }
}
