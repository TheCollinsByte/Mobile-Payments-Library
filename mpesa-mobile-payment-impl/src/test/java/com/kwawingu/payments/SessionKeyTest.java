/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SessionKeyTest {

  private SessionKey mpesaSessionKey;
  private EncryptApiKey encryptApiKey;
  private ApiEndpoint apiEndpoint;

  @BeforeEach
  public void setUp() {
    String publicKey = System.getenv("MPESA_PUBLIC_KEY");
    String apiKey = System.getenv("MPESA_API_KEY");

    if (publicKey == null || apiKey == null) {
      throw new RuntimeException(
          "Missing environment variables: MPESA_PUBLIC_KEY or MPESA_API_KEY");
    }

    mpesaSessionKey = new SessionKey();
    encryptApiKey = new EncryptApiKey(publicKey, apiKey);
    Market vodacomTZN = Market.VODACOM_TANZANIA;
    Environment sandboxEnv = Environment.SANDBOX;
    apiEndpoint = new ApiEndpoint(sandboxEnv, vodacomTZN);
  }

  @Test
  public void testClientGetSessionKey() throws IOException {
    String context = apiEndpoint.getUrl(Service.GET_SESSION);
    String encryptApiKey = this.encryptApiKey.generateAnEncryptApiKey();
    assertNotNull(encryptApiKey);

    Optional<String> session = mpesaSessionKey.getSessionKey(encryptApiKey, context);

    assertTrue(session.isPresent());
  }

  @Test
  public void testGetSessionKey_SuccessfulResponse() {}

  @Test
  public void testGetSessionKey_ErrorResponse() {}

  @Test
  public void testGetSessionKey_Exception() {}
}
