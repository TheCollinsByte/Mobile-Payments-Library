package com.kwawingu.payments;

import com.kwawingu.payments.C2B.CustomerToBusinessTransaction;
import com.kwawingu.payments.C2B.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerToBusinessTransactionTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransactionTest.class);
    private CustomerToBusinessTransaction customerToBusinessTransaction;

    Payload payload = new Payload.Builder()
            .setAmount("10.00")
            .setCustomerMSISDN("+255762578467")
            .setCountry(Market.VODACOM_TANZANIA.getInputCountryValue())
            .setCurrency(Market.VODACOM_TANZANIA.getInputCurrencyValue())
            .setServiceProviderCode("000000")
            .setTransactionReference("T1234C")
            .setThirdPartyConversationID("asv02e5958774f783d0d689761")
            .setPurchasedItemsDesc("Handbag, Black shoes")
            .build();

    @BeforeEach
    public void setUp() {
        String publicKey = System.getenv("MPESA_PUBLIC_KEY");
        String apiKey = System.getenv("MPESA_API_KEY");

        if (publicKey == null || apiKey == null) {
            throw new RuntimeException(
                    "Missing environment variables: MPESA_PUBLIC_KEY or MPESA_API_KEY");
        }

        ApiEndpoint apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
        GenerateSessionKey sessionKey = new GenerateSessionKey();
        EncryptApiKey encryptApiKey = new EncryptApiKey(publicKey, apiKey);

        String context = apiEndpoint.getUrl(Service.GET_SESSION);
        String anEncryptedApiKey = encryptApiKey.generateAnEncryptApiKey();
        LOG.info(context);

        assertNotNull(anEncryptedApiKey);
        LOG.info(payload.toJsonString());

        Optional<String> generatedSessionKey = sessionKey.getSessionKey(anEncryptedApiKey, context);
        assertTrue(generatedSessionKey.isPresent());
        String encryptedSessionKey = encryptApiKey.generateAnEncryptSessionKey(generatedSessionKey.orElse(null));
        customerToBusinessTransaction = new CustomerToBusinessTransaction.Builder()
                .setApiEndpoint(apiEndpoint)
                .setEncryptedSessionKey(encryptedSessionKey)
                .setPayload(payload)
                .build();
    }

    private void printSanitizeResponse(String response) {
        for (String s : response.split(",")) {
            if (s.contains("output_ResponseDesc")){
                LOG.info(s.trim());
            }
        }
        LOG.info(Arrays.stream(response.split(",")).toList().toString());
    }

    @Test
    public void testPayment_wheInitiated_responseSucceed() throws IOException, InterruptedException {
        String response = customerToBusinessTransaction.initiatePayment();
        printSanitizeResponse(response);

        assertNotNull(response);
        assertFalse(response.isBlank());
    }

}
