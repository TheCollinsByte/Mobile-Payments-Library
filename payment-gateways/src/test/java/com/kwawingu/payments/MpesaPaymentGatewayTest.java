package com.kwawingu.payments;

import com.kwawingu.payments.mpesa.MpesaPaymentGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        Assertions.assertNotNull(transaction);
        Assertions.assertTrue(transaction.isSuccess());
        Assertions.assertEquals("mock_transaction_id", transaction.getTransactionId());
        Assertions.assertEquals(1000, transaction.getAmount());
        Assertions.assertEquals("TZS", transaction.getCurrency());
    }
}
