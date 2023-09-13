package com.kwawingu.payments;

public interface PaymentGateway {
    PaymentTransaction submitPayment(double amount, String currency, String paymentMethod);
}
