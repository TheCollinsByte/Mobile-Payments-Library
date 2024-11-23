package com.kwawingu.payments.client.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class BusinessToBusinessTransactionResponse {

    public static class SynchronousResponse {

        @SerializedName("output_ResponseCode")
        private String responseCode;

        @SerializedName("output_ResponseDesc")
        private String responseDesc;

        @SerializedName("output_TransactionID")
        private String transactionID;

        @SerializedName("output_ConversationID")
        private String conversationID;

        @SerializedName("output_ThirdPartyConversationID")
        private String thirdPartyConversationID;

        public String getResponseCode() {
            return responseCode;
        }

        public String getResponseDesc() {
            return responseDesc;
        }

        public String getTransactionID() {
            return transactionID;
        }

        public String getConversationID() {
            return conversationID;
        }

        public String getThirdPartyConversationID() {
            return thirdPartyConversationID;
        }

        public String toJson() {
            return new Gson().toJson(this);
        }

        public static SynchronousResponse fromJson(String json) {
            return new Gson().fromJson(json, SynchronousResponse.class);
        }
    }

    public static class AsynchronousInitialResponse {

        @SerializedName("output_ResponseCode")
        private String responseCode;

        @SerializedName("output_ResponseDesc")
        private String responseDesc;

        @SerializedName("output_ConversationID")
        private String conversationID;

        @SerializedName("output_ThirdPartyConversationID")
        private String thirdPartyConversationID;

        public String getResponseCode() {
            return responseCode;
        }

        public String getResponseDesc() {
            return responseDesc;
        }

        public String getConversationID() {
            return conversationID;
        }

        public String getThirdPartyConversationID() {
            return thirdPartyConversationID;
        }

        public String toJson() {
            return new Gson().toJson(this);
        }

        public static AsynchronousInitialResponse fromJson(String json) {
            return new Gson().fromJson(json, AsynchronousInitialResponse.class);
        }
    }

    public static class AsynchronousFinalResponse {

        @SerializedName("input_OriginalConversationID")
        private String originalConversationID;

        @SerializedName("input_TransactionID")
        private String transactionID;

        @SerializedName("input_ResultCode")
        private String resultCode;

        @SerializedName("input_ResultDesc")
        private String resultDesc;

        @SerializedName("input_ThirdPartyConversationID")
        private String thirdPartyConversationID;

        public String getOriginalConversationID() {
            return originalConversationID;
        }

        public String getTransactionID() {
            return transactionID;
        }

        public String getResultCode() {
            return resultCode;
        }

        public String getResultDesc() {
            return resultDesc;
        }

        public String getThirdPartyConversationID() {
            return thirdPartyConversationID;
        }

        public String toJson() {
            return new Gson().toJson(this);
        }

        public static AsynchronousFinalResponse fromJson(String json) {
            return new Gson().fromJson(json, AsynchronousFinalResponse.class);
        }
    }

    public static class AsynchronousFinalConfirmation {

        @SerializedName("output_OriginalConversationID")
        private String originalConversationID;

        @SerializedName("output_ResponseCode")
        private String responseCode;

        @SerializedName("output_ResponseDesc")
        private String responseDesc;

        @SerializedName("output_ThirdPartyConversationID")
        private String thirdPartyConversationID;

        public AsynchronousFinalConfirmation(String originalConversationID, String thirdPartyConversationID) {
            this.originalConversationID = originalConversationID;
            this.responseCode = "0"; // Always "0" for successful confirmation
            this.responseDesc = "Successfully Accepted Result";
            this.thirdPartyConversationID = thirdPartyConversationID;
        }

        public String getOriginalConversationID() {
            return originalConversationID;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public String getResponseDesc() {
            return responseDesc;
        }

        public String getThirdPartyConversationID() {
            return thirdPartyConversationID;
        }

        public String toJson() {
            return new Gson().toJson(this);
        }

        public static AsynchronousFinalConfirmation fromJson(String json) {
            return new Gson().fromJson(json, AsynchronousFinalConfirmation.class);
        }
    }
}