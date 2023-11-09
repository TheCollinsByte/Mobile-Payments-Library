package com.kwawingu.payments.client.response;

import com.kwawingu.payments.session.keys.MpesaSessionKey;

import java.net.http.HttpResponse;

public class GetSessionResponse extends MpesaHttpResponse {

    private final MpesaSessionKey output_SessionID;

    protected GetSessionResponse(HttpResponse<String> response) {
        super("", "");
        output_SessionID = new MpesaSessionKey("");
    }
}
