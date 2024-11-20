package com.kwawingu.payments.b2b;

import com.kwawingu.payments.ApiEndpoint;
import com.kwawingu.payments.Environment;
import com.kwawingu.payments.Market;
import com.kwawingu.payments.client.payload.BusinessToBusinessPayload;
import com.kwawingu.payments.exception.SessionKeyUnavailableException;
import com.kwawingu.payments.session.MpesaSession;
import com.kwawingu.payments.session.keys.MpesaEncryptedSessionKey;
import com.kwawingu.payments.session.provider.MpesaKeyProviderFromEnvironment;
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

import static org.junit.jupiter.api.Assertions.*;

public class BusinessToBusinessTransactionTest {
    private static final Logger LOG =
            LoggerFactory.getLogger(BusinessToBusinessTransactionTest.class);

    private BusinessToBusinessTransaction transaction;

    @BeforeEach
    public void setUp()
            throws NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException,
            SessionKeyUnavailableException {
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

        BusinessToBusinessPayload payload = new BusinessToBusinessPayload.Builder()
                .setAmount("10.00")
                .setCountry(Market.VODACOM_TANZANIA.getInputCountryValue())
                .setCurrency(Market.VODACOM_TANZANIA.getInputCurrencyValue())
                .setPrimaryPartyCode("ORG002")
                .setReceiverPartyCode("ORG001")
                .setTransactionReference("T12344C")
                .setThirdPartyConversationID("1e9b774d1da34af78412a498cbc28f5e")
                .setPurchasedItemsDesc("Handbag, Black shoes")
                .build();

        transaction = new BusinessToBusinessTransaction.Builder()
                .setApiEndpoint(new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA))
                .setEncryptedSessionKey(session.getEncryptedSessionKey())
                .setPayload(payload)
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