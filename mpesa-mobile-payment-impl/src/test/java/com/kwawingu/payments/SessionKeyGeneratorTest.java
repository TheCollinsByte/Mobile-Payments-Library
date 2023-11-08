/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.kwawingu.payments.session.Config;
import com.kwawingu.payments.session.keys.MpesaApiKey;
import com.kwawingu.payments.session.keys.MpesaPublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionKeyGeneratorTest {
  private static final Logger LOG = LoggerFactory.getLogger(SessionKeyGeneratorTest.class);

  private SessionKeyGenerator mpesaSessionKeyGenerator;
  private EncryptApiKey encryptApiKey;
  private ApiEndpoint apiEndpoint;

  @BeforeEach
  public void setUp() {
    Config config = new Config(new MpesaApiKey("MPESA_API_KEY"), new MpesaPublicKey("MPESA_PUBLIC_KEY"));
    mpesaSessionKeyGenerator = new SessionKeyGenerator();
    encryptApiKey = new EncryptApiKey(config);
    apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
  }

  @Test
  public void testClientGetSessionKey() {
    String context = apiEndpoint.getUrl(Service.GET_SESSION);
    LOG.info(context);
    String encryptedApiKey = encryptApiKey.generateAnEncryptApiKey();
    assertNotNull(encryptedApiKey);
    String sessionKey = mpesaSessionKeyGenerator.getSessionKeyOrThrowUnchecked(encryptedApiKey, context);
    assertNotNull(sessionKey);
  }
}
