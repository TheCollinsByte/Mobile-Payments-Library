package com.kwawingu.payments.client.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kwawingu.payments.c2b.CustomerToBusinessTransaction;
import com.kwawingu.payments.session.keys.MpesaSessionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpResponse;

public class GetSessionResponse extends MpesaHttpResponse {
    @SuppressWarnings("UnusedVariable")
    private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransaction.class);

    private final MpesaSessionKey output_SessionID;

    private final int httpStatusCode;

    public GetSessionResponse(int statusCode, JsonObject jsonObject) {
        super(jsonObject.get("output_ResponseCode").getAsString(), jsonObject.get("output_ResponseDesc").getAsString());
        output_SessionID = new MpesaSessionKey(jsonObject.get("output_SessionID").getAsString());
        httpStatusCode = statusCode;
    }

    public MpesaSessionKey getOutput_SessionID() {
        return output_SessionID;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
