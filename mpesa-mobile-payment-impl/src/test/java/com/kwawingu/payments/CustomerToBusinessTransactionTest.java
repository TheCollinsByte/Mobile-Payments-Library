/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kwawingu.payments.c2b.CustomerToBusinessTransaction;
import com.kwawingu.payments.client.payload.Payload;
import com.kwawingu.payments.exception.SessionKeyUnavailableException;
import com.kwawingu.payments.session.MpesaKeyProviderFromEnvironment;
import com.kwawingu.payments.session.MpesaSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerToBusinessTransactionTest {
  private static final Logger LOG =
      LoggerFactory.getLogger(CustomerToBusinessTransactionTest.class);

  private final MpesaKeyProviderFromEnvironment.Config config =
      new MpesaKeyProviderFromEnvironment.Config.Builder()
          .setApiKeyEnvName("MPESA_API_KEY")
          .setPublicKeyEnvName("MPESA_PUBLIC_KEY")
          .build();

  private final MpesaSession session =
      new MpesaSession(
          new MpesaKeyProviderFromEnvironment(config),
          Environment.SANDBOX,
          Market.VODACOM_TANZANIA);

  private void printSanitizeResponse(String response) {
    for (String s : response.split(",")) {
      if (s.contains("output_ResponseDesc")) {
        LOG.info(s.trim());
      }
    }
    LOG.info(Arrays.stream(response.split(",")).toList().toString());
  }

  @Test
  public void testPayment_wheInitiated_responseSucceed()
          throws IOException,
          InterruptedException,
          SessionKeyUnavailableException,
          NoSuchPaddingException,
          IllegalBlockSizeException,
          NoSuchAlgorithmException,
          InvalidKeySpecException,
          BadPaddingException,
          InvalidKeyException, URISyntaxException {
    // Set-Up
    Payload payload =
        new Payload.Builder()
            .setAmount("10.00")
            .setCustomerMSISDN("254707161122")
            .setCountry(Market.VODAFONE_GHANA.getInputCountryValue())
            .setCurrency(Market.VODAFONE_GHANA.getInputCurrencyValue())
            .setServiceProviderCode("ORG001")
            .setTransactionReference("T12344C")
            .setThirdPartyConversationID("1e9b774d1da34af78412a498cbc28f5e")
            .setPurchasedItemsDesc("Handbag, Black shoes")
            .build();

    CustomerToBusinessTransaction customerToBusinessTransaction =
        new CustomerToBusinessTransaction.Builder()
            .setApiEndpoint(new ApiEndpoint(Environment.SANDBOX, Market.VODAFONE_GHANA))
            .setEncryptedSessionKey(session.getEncryptedSessionKey())
            .setPayload(payload)
            .build();

    // Test
    String response = customerToBusinessTransaction.synchronousPayment();
    printSanitizeResponse(response);

    // Assertion
    assertNotNull(response);
    assertFalse(response.isBlank());
  }
}
