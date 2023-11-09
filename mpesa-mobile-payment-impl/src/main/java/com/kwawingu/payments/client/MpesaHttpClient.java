package com.kwawingu.payments.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class MpesaHttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(MpesaHttpClient.class);

    private final HttpClient httpClient;

    public MpesaHttpClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> getRequest(Map<String, String> headers, URI uri) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest
                .newBuilder()
                .uri(uri)
                .GET();
        headers.forEach(requestBuilder::headers);
        HttpRequest request = requestBuilder.build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> postRequest(Map<String, String> headers, HttpRequest.BodyPublisher httpBody, URI uri) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest
                .newBuilder()
                .uri(uri)
                .POST(httpBody);
        headers.forEach(requestBuilder::headers);
        HttpRequest request = requestBuilder.build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
