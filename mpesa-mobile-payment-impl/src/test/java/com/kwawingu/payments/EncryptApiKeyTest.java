/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kwawingu.payments.session.provider.MpesaKeyProvider;
import com.kwawingu.payments.session.provider.MpesaKeyProviderFromEnvironment;
import com.kwawingu.payments.session.keys.MpesaEncryptedApiKey;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptApiKeyTest {

  private static final Logger LOG = LoggerFactory.getLogger(EncryptApiKeyTest.class);

  private HttpClient httpClient;
  private ApiEndpoint apiEndpoint;
  private MpesaEncryptedApiKey mpesaEncryptedApiKey;
  private MpesaKeyProvider mpesaKeyProvider;

  @BeforeEach
  public void setUp() {
    MpesaKeyProviderFromEnvironment.Config config =
        new MpesaKeyProviderFromEnvironment.Config.Builder()
            .setApiKeyEnvName("MPESA_API_KEY")
            .setPublicKeyEnvName("MPESA_PUBLIC_KEY")
            .build();
    mpesaKeyProvider = new MpesaKeyProviderFromEnvironment(config);
    httpClient = HttpClient.newHttpClient();
    apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
  }

  /**
   * Apart from checking for Null, Needs to implement decryption logic to validate the session key.
   */
  @Test
  public void testGetASessionId()
      throws IOException,
          InterruptedException,
          NoSuchPaddingException,
          IllegalBlockSizeException,
          NoSuchAlgorithmException,
          InvalidKeySpecException,
          BadPaddingException,
          InvalidKeyException {
    MpesaEncryptedApiKey encryptedApiKey =
        mpesaKeyProvider.getApiKey().encrypt(mpesaKeyProvider.getPublicKey());

    URI context = apiEndpoint.getUrl(Service.GET_SESSION);
    LOG.info(String.valueOf(context));

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    encryptedApiKey.insertAuthorizationHeader(headers);
    headers.put("Origin", "*");

    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(context).GET();
    headers.forEach(requestBuilder::headers);
    HttpRequest request = requestBuilder.build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200 && response.statusCode() != 400) {
      throw new IOException("Unexpected HTTP Code: " + response.statusCode());
    }

    if (response.statusCode() == 200) {
      String responseBody = response.body();
      String[] apiResponse = responseBody.split(",");
      assertEquals("{\"output_ResponseCode\":\"INS-0\"", apiResponse[0]);
      assertEquals("\"output_ResponseDesc\":\"Request processed successfully\"", apiResponse[1]);
    }

    if (response.statusCode() == 400) {
      throw new IOException("Session Creation Failed: " + response.statusCode());
    }
  }
}
