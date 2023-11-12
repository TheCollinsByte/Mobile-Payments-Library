package com.kwawingu.payments.client.response;

public abstract class MpesaHttpResponse {
    private final String output_ResponseCode;
    private final String output_ResponseDesc;

    protected MpesaHttpResponse(String outputResponseCode, String output_ResponseDesc) {
        this.output_ResponseCode = outputResponseCode;
        this.output_ResponseDesc = output_ResponseDesc;
    }
}