/*
 * Copyright 2021-2024 KwaWingu.
 */
package com.kwawingu.payments.c2b;

import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.Environment;
import com.kwawingu.payments.Market;
import com.kwawingu.payments.client.payload.CustomerToBusinessPayload;
import com.kwawingu.payments.exception.SessionKeyUnavailableException;
import com.kwawingu.payments.session.MpesaSession;
import com.kwawingu.payments.session.provider.MpesaKeyProviderFromEnvironment;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class CustomerToBusinessTransactionTest {
  private static final Logger LOG =
          LoggerFactory.getLogger(CustomerToBusinessTransactionTest.class);

  private CustomerToBusinessTransaction transaction;

  @BeforeEach
  public void setUp()
          throws NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException,
          IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException,
          SessionKeyUnavailableException, URISyntaxException {
    MpesaKeyProviderFromEnvironment.Config config =
            new MpesaKeyProviderFromEnvironment.Config.Builder()
                    .setApiKeyEnvName("MPESA_API_KEY")
                    .setPublicKeyEnvName("MPESA_PUBLIC_KEY")
                    .build();

    MpesaSession session = new MpesaSession(
            new MpesaKeyProviderFromEnvironment(config),
            Environment.SANDBOX,
            Market.VODACOM_TANZANIA
    );

    CustomerToBusinessPayload customerToBusinessPayload = new CustomerToBusinessPayload.Builder()
            .setAmount("10.00")
            .setCustomerMSISDN("+255 762578467")
            .setCountry(Market.VODACOM_TANZANIA.getInputCountryValue())
            .setCurrency(Market.VODACOM_TANZANIA.getInputCurrencyValue())
            .setServiceProviderCode("ORG001")
            .setTransactionReference("T12344C")
            .setThirdPartyConversationID("1e9b774d1da34af78412a498cbc28f5e")
            .setPurchasedItemsDesc("Handbag, Black shoes")
            .build();

    transaction = new CustomerToBusinessTransaction.Builder()
            .setApiEndpoint(new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA))
            .setEncryptedSessionKey(session.getEncryptedSessionKey())
            .setPayload(customerToBusinessPayload)
            .build();
  }

  @Test
  public void testSynchronousPayment() {
    try {
      String response = transaction.synchronousPayment();
      assertNotNull(response);
      assertFalse(response.isBlank());
      printSanitizeResponse(response);
    } catch (IOException | InterruptedException e) {
      LOG.error("Exception thrown during transaction: {}", e.getMessage());
      fail("Exception thrown: " + e.getMessage());
    }
  }

  private void printSanitizeResponse(String response) {
    for (String s : response.split(",")) {
      if (s.contains("output_ResponseDesc")) {
        LOG.info(s.trim());
      }
    }
    LOG.info(Arrays.stream(response.split(",")).toList().toString());
  }
}