package com.kwawingu.payments;

public class PaymentTransaction {
    private String transactionId;
    private double amount;
    private String currency;
    private boolean isSuccess;

    public PaymentTransaction() {
    }

    public PaymentTransaction(String transactionId, double amount, String currency, boolean isSuccess) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.isSuccess = isSuccess;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
