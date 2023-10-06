package com.kwawingu.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerToBusinessTransactionTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransactionTest.class);
    private EncryptApiKey encryptApiKey;
    private ApiEndpoint apiEndpoint;
    private CustomerToBusinessTransaction customerToBusinessTransaction;

    @BeforeEach
    public void setUp() {
        String publicKey = System.getenv("MPESA_PUBLIC_KEY");
        String apiKey = System.getenv("MPESA_API_KEY");

        if (publicKey == null || apiKey == null) {
            throw new RuntimeException(
                    "Missing environment variables: MPESA_PUBLIC_KEY or MPESA_API_KEY");
        }
        encryptApiKey = new EncryptApiKey(publicKey, apiKey);
        apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
        customerToBusinessTransaction = new CustomerToBusinessTransaction(encryptApiKey, apiEndpoint);
    }

    @Test
    public void testCustomerToBusinessPayment() throws IOException, InterruptedException {
        String response = customerToBusinessTransaction.initiatePayment();
        LOG.info(response);
        assertNotNull(response);
        assertTrue(response.isBlank());
    }

}
