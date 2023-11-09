/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.session.keys;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MpesaApiKey {
  private final String apiKey;

  public MpesaApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public MpesaEncryptedApiKey encrypt(MpesaPublicKey publicKey)
      throws NoSuchAlgorithmException,
          InvalidKeySpecException,
          NoSuchPaddingException,
          InvalidKeyException,
          IllegalBlockSizeException,
          BadPaddingException {
    PublicKey pubKey = publicKey.toRsaPublicKey();
    Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] encryptedBytes = rsaCipher.doFinal(apiKey.getBytes());
    return new MpesaEncryptedApiKey(Base64.getEncoder().encodeToString(encryptedBytes));
  }
}
