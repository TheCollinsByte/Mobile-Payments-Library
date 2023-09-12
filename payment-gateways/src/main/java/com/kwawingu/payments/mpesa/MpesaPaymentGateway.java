package com.kwawingu.payments.mpesa;

import com.kwawingu.payments.PaymentGateway;
import com.kwawingu.payments.PaymentTransaction;

public class MpesaPaymentGateway implements PaymentGateway {
    public String apiKey;

    public MpesaPaymentGateway(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public PaymentTransaction submitPayment(double amount, String currency, String paymentMethod) {

        boolean isSuccess = true;   // We Assume payment is successful
        String transactionId = "mock_transaction_id";   // We Assume payment is successful


        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setSuccess(isSuccess);

        return transaction;
    }
}