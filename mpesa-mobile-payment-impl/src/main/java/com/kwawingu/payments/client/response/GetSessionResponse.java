package com.kwawingu.payments.client.response;

import com.kwawingu.payments.session.keys.MpesaSessionKey;

import java.net.http.HttpResponse;

public class GetSessionResponse {
    private final MpesaSessionKey output_SessionID;

    protected GetSessionResponse(HttpResponse<String> response) {
        output_SessionID = new MpesaSessionKey("");
    }
}
