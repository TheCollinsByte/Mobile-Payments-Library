package com.kwawingu.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerToBusinessTransactionTest {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransactionTest.class);
    private CustomerToBusinessTransaction customerToBusinessTransaction;

    @BeforeEach
    public void setUp() {
        String sessionKey = System.getenv("MPESA-SESSION-KEY");
        if (sessionKey == null) {
            throw new RuntimeException(
                    "Missing environment variables: MPESA_API_KEY");
        }
        ApiEndpoint apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
        customerToBusinessTransaction = new CustomerToBusinessTransaction(apiEndpoint, sessionKey);
    }

    @Test
    public void testCustomerToBusinessPayment() throws IOException, InterruptedException {
        String response = customerToBusinessTransaction.initiatePayment();
        LOG.info(response);
        assertNotNull(response);
        assertFalse(response.isBlank());
    }

}
