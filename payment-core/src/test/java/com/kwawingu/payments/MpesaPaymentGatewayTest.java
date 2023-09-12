package com.kwawingu.payments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MpesaPaymentGatewayTest {
    private MpesaPaymentGateway gateway;

    @BeforeEach
    public void setUp() {
        // Initialize the gateway with a mock API key
        gateway = new MpesaPaymentGateway("mock-api-key");
    }

    @Test
    public void testSubmitPaymentSuccess() {
        PaymentTransaction transaction = gateway.submitPayment(1000, "TZS", "phone number");

        assertNotNull(transaction);
        assertTrue(transaction.isSuccess());
        assertEquals("mock_transaction_id", transaction.getTransactionId());
        assertEquals(1000, transaction.getAmount());
        assertEquals("TZS", transaction.getCurrency());
    }
}
