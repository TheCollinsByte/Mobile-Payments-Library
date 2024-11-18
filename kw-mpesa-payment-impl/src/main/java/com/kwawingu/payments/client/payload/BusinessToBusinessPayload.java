package com.kwawingu.payments.client.payload;

import com.google.gson.Gson;
import java.util.Objects;

public class BusinessToBusinessPayload {

    private final String inputAmount;
    private final String inputCountry;
    private final String inputCurrency;
    private final String inputPrimaryPartyCode;
    private final String inputReceiverPartyCode;
    private final String inputTransactionReference;
    private final String inputThirdPartyConversationID;
    private final String inputPurchasedItemsDesc;

    private BusinessToBusinessPayload(Builder builder) {
        this.inputAmount = builder.inputAmount;
        this.inputCountry = builder.inputCountry;
        this.inputCurrency = builder.inputCurrency;
        this.inputPrimaryPartyCode = builder.inputPrimaryPartyCode;
        this.inputReceiverPartyCode = builder.inputReceiverPartyCode;
        this.inputTransactionReference = builder.inputTransactionReference;
        this.inputThirdPartyConversationID = builder.inputThirdPartyConversationID;
        this.inputPurchasedItemsDesc = builder.inputPurchasedItemsDesc;
    }

    public static class Builder {
        private String inputAmount;
        private String inputCountry;
        private String inputCurrency;
        private String inputPrimaryPartyCode;
        private String inputReceiverPartyCode;
        private String inputTransactionReference;
        private String inputThirdPartyConversationID;
        private String inputPurchasedItemsDesc;

        public Builder setAmount(String amount) {
            this.inputAmount = amount;
            return this;
        }

        public Builder setCountry(String country) {
            this.inputCountry = country;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.inputCurrency = currency;
            return this;
        }

        public Builder setPrimaryPartyCode(String primaryPartyCode) {
            this.inputPrimaryPartyCode = primaryPartyCode;
            return this;
        }

        public Builder setReceiverPartyCode(String receiverPartyCode) {
            this.inputReceiverPartyCode = receiverPartyCode;
            return this;
        }

        public Builder setTransactionReference(String transactionReference) {
            this.inputTransactionReference = transactionReference;
            return this;
        }

        public Builder setThirdPartyConversationID(String thirdPartyConversationID) {
            this.inputThirdPartyConversationID = thirdPartyConversationID;
            return this;
        }

        public Builder setPurchasedItemsDesc(String purchasedItemsDesc) {
            this.inputPurchasedItemsDesc = purchasedItemsDesc;
            return this;
        }

        public BusinessToBusinessPayload build() {
            Objects.requireNonNull(inputAmount, "Amount cannot be null");
            Objects.requireNonNull(inputCountry, "Country cannot be null");
            Objects.requireNonNull(inputCurrency, "Currency cannot be null");
            Objects.requireNonNull(inputPrimaryPartyCode, "Primary Party Code cannot be null");
            Objects.requireNonNull(inputReceiverPartyCode, "Receiver Party Code cannot be null");
            Objects.requireNonNull(inputTransactionReference, "Transaction Reference cannot be null");
            Objects.requireNonNull(inputThirdPartyConversationID, "Third Party Conversation ID cannot be null");
            Objects.requireNonNull(inputPurchasedItemsDesc, "Purchased Items Description cannot be null");

            return new BusinessToBusinessPayload(this);
        }
    }

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}