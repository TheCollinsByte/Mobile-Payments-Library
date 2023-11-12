package com.kwawingu.payments.client.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kwawingu.payments.session.keys.MpesaSessionKey;

import java.net.http.HttpResponse;

public class GetSessionResponse extends MpesaHttpResponse {
    private MpesaSessionKey output_SessionID;

    public GetSessionResponse(HttpResponse<String> response) {
        JsonObject jsonObject = util(response.body());
        super(jsonObject.get("output_ResponseCode").getAsString(), jsonObject.get("output_ResponseDesc").getAsString());
        output_SessionID = new MpesaSessionKey(jsonObject.get("output_SessionID").getAsString());
    }

    public JsonObject util(String response) {
        try {
            JsonElement jsonElement = JsonParser.parseString(response);
            if (jsonElement.isJsonObject())
                return jsonElement.getAsJsonObject();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return new JsonObject();
    }
}
