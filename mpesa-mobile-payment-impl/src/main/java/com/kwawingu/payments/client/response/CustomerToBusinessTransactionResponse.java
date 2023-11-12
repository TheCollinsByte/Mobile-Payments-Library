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
        private final int rawHttpStatusCode;

        public SynchronousResponses(int rawHttpStatusCode, JsonObject jsonObject) {
            super(jsonObject.get("output_ResponseCode").getAsString(), jsonObject.get("output_ResponseDesc").getAsString());
            this.output_ConversationID = jsonObject.get("output_ConversationID").getAsString();
            this.output_TransactionID = jsonObject.get("output_TransactionID").getAsString();
            this.output_ThirdPartyConversationID = jsonObject.get("output_ThirdPartyConversationID").getAsString();
            this.rawHttpStatusCode = rawHttpStatusCode;
        }

        public String toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("output_ResponseCode", getOutput_ResponseCode());
            json.addProperty("output_ResponseDesc", getOutput_ResponseDesc());
            json.addProperty("output_ConversationID", output_ConversationID);
            json.addProperty("output_TransactionID", output_TransactionID);
            json.addProperty("output_ThirdPartyConversationID", output_ThirdPartyConversationID);
            json.addProperty("rawHttpStatusCode", rawHttpStatusCode);
            return json.toString();
        }
    }

    public class  AsynchronousResponses extends MpesaHttpResponse {
        @SuppressWarnings("UnusedVariable")
        private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransaction.class);

        private final String output_ConversationID;
        private final String output_ThirdPartyConversationID;
        private final int rawHttpStatusCode;

        protected AsynchronousResponses(int rawHttpStatusCode, JsonObject jsonObject) {
            super(jsonObject.get("output_ResponseCode").getAsString(), jsonObject.get("output_ResponseDesc").getAsString());
            output_ConversationID = jsonObject.get("output_ConversationID").getAsString();
            output_ThirdPartyConversationID = jsonObject.get("output_ThirdPartyConversationID").getAsString();
            this.rawHttpStatusCode = rawHttpStatusCode;
        }

        public String toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("output_ResponseCode", getOutput_ResponseCode());
            json.addProperty("output_ResponseDesc", getOutput_ResponseDesc());
            json.addProperty("output_ConversationID", output_ConversationID);
            json.addProperty("output_ThirdPartyConversationID", output_ThirdPartyConversationID);
            json.addProperty("rawHttpStatusCode", rawHttpStatusCode);
            return json.toString();
        }
    }
}
