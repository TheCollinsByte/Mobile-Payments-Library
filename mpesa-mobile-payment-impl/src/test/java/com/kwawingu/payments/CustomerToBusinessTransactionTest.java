package com.kwawingu.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerToBusinessTransactionTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransactionTest.class);
    private CustomerToBusinessTransaction customerToBusinessTransaction;

    @BeforeEach
    public void setUp() {
        String publicKey = System.getenv("MPESA_PUBLIC_KEY");
        String apiKey = System.getenv("MPESA_API_KEY");

        if (publicKey == null || apiKey == null) {
            throw new RuntimeException(
                    "Missing environment variables: MPESA_PUBLIC_KEY or MPESA_API_KEY");
        }

        ApiEndpoint apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
        SessionKey mpesasessionKey = new SessionKey();
        EncryptApiKey encryptApiKey = new EncryptApiKey(publicKey, apiKey);

        String context = apiEndpoint.getUrl(Service.GET_SESSION);
        String anEncryptedApiKey = encryptApiKey.generateAnEncryptApiKey();

        LOG.info(context);
        LOG.info(anEncryptedApiKey);

        Optional<String> sessionKey = mpesasessionKey.getSessionKey(anEncryptedApiKey, context);

        customerToBusinessTransaction = new CustomerToBusinessTransaction(apiEndpoint, sessionKey.orElse(null), encryptApiKey);
    }

    @Test
    public void testCustomerToBusinessPayment() throws IOException, InterruptedException {
        String response = customerToBusinessTransaction.initiatePayment();
        LOG.info(response);
        assertNotNull(response);
        assertFalse(response.isBlank());
    }

}
