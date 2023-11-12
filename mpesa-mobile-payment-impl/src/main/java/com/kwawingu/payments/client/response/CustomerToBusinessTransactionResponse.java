package com.kwawingu.payments.client.response;

import com.google.gson.JsonObject;
import com.kwawingu.payments.c2b.CustomerToBusinessTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerToBusinessTransactionResponse {
    public static class  SynchronousResponses extends MpesaHttpResponse {
        @SuppressWarnings("UnusedVariable")
        private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransaction.class);

        private final String output_TransactionID;
        private final String output_ConversationID;
        private final String output_ThirdPartyConversationID;

        public SynchronousResponses(int httpStatusCode, JsonObject jsonObject) {
            super(jsonObject.get("output_ResponseCode").getAsString(), jsonObject.get("output_ResponseDesc").getAsString());
            output_ConversationID = jsonObject.get("output_ConversationID").getAsString();
            output_TransactionID = jsonObject.get("output_TransactionID").getAsString();
            output_ThirdPartyConversationID = jsonObject.get("output_ThirdPartyConversationID").getAsString();
        }

        public String getOutput_ConversationID() {
            return output_ConversationID;
        }

        public String getOutput_ThirdPartyConversationID() {
            return output_ThirdPartyConversationID;
        }

        public String getOutput_TransactionID() {
            return output_TransactionID;
        }
    }

    public class  AsynchronousResponses extends MpesaHttpResponse {
        @SuppressWarnings("UnusedVariable")
        private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransaction.class);

        private final String output_ConversationID;
        private final String output_ThirdPartyConversationID;

        protected AsynchronousResponses(int httpStatusCode, JsonObject jsonObject) {
            super(jsonObject.get("output_ResponseCode").getAsString(), jsonObject.get("output_ResponseDesc").getAsString());
            output_ConversationID = jsonObject.get("output_ConversationID").getAsString();
            output_ThirdPartyConversationID = jsonObject.get("output_ThirdPartyConversationID").getAsString();
        }

        public String getOutput_ConversationID() {
            return output_ConversationID;
        }

        public String getOutput_ThirdPartyConversationID() {
            return output_ThirdPartyConversationID;
        }
    }
}
