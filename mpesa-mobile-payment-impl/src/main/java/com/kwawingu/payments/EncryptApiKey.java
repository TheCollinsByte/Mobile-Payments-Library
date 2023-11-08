/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.kwawingu.payments.session.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptApiKey {
  private static final Logger LOG = LoggerFactory.getLogger(EncryptApiKey.class);

  private final Config config;

  public EncryptApiKey(Config config) {
    this.config = config;
  }

  private String encryptPublicKeyAsBase64(String apiKey) {
    byte[] publicKeyBytes = Base64.getDecoder().decode(config.getMpesaPublicKey().getPublicKey());
    try {
      PublicKey pubKey =
              KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
      Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
      byte[] encryptedBytes = rsaCipher.doFinal(apiKey.getBytes());
      return Base64.getEncoder().encodeToString(encryptedBytes);
    } catch (NoSuchAlgorithmException
             | InvalidKeySpecException
             | NoSuchPaddingException
             | InvalidKeyException
             | IllegalBlockSizeException
             | BadPaddingException e) {
      throw new RuntimeException(e);
    }
  }

  public String generateAnEncryptApiKey() {
    return encryptPublicKeyAsBase64(config.getMpesaApiKey().getApiKey());
  }

  public String generateAnEncryptedSessionId(String session) {
    return encryptPublicKeyAsBase64(session);
  }
}
