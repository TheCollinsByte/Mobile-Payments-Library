/*
 * Copyright 2021-2024 KwaWingu.
 */
package com.kwawingu.payments.client;

import com.google.gson.*;
import com.kwawingu.payments.client.response.BusinessToBusinessTransactionResponse;
import com.kwawingu.payments.client.response.CustomerToBusinessTransactionResponse;
import com.kwawingu.payments.client.response.GetSessionResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MpesaHttpClient {
  @SuppressWarnings("UnusedVariable")
  private static final Logger LOG = LoggerFactory.getLogger(MpesaHttpClient.class);

  private final HttpClient httpClient;
  private final Gson gson;

  public MpesaHttpClient() {
    this.httpClient = HttpClient.newHttpClient();
    this.gson = new Gson();
  }

  public JsonObject stringToJson(String response) {
    try {
      return gson.fromJson(response, JsonObject.class);
    } catch (JsonParseException e) {
      LOG.error("Failed to parse response to JSON", e);
      throw new IllegalStateException(e);
    }
  }

  public <T> T sendGetRequest(Map<String, String> headers, URI uri, Class<T> responseType) throws IOException, InterruptedException {
    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri).GET();
    headers.forEach(requestBuilder::header);
    HttpRequest request = requestBuilder.build();
    HttpResponse<String> response = sendRequest(request);
    return parseResponse(response, responseType);
  }


  public <T> T sendPostRequest(Map<String, String> headers,
                               HttpRequest.BodyPublisher httpBody,
                               URI uri,
                               Class<T> responseType) throws IOException, InterruptedException {

    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri).POST(httpBody);
    headers.forEach(requestBuilder::header);
    HttpRequest request = requestBuilder.build();
    HttpResponse<String> response = sendRequest(request);
    return parseResponse(response, responseType);
  }


  private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      LOG.error("HTTP request failed", e);
      throw e;
    }
  }

  private <T> T parseResponse(HttpResponse<String> response, Class<T> responseType) throws IOException{
    try {
      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        return gson.fromJson(response.body(), responseType);
      } else {
        LOG.error("HTTP request failed with status code: {}", response.statusCode());
        throw new IOException("Request failed with status code: " + response.statusCode());
      }
    } catch (JsonParseException e) {
      LOG.error("Failed to parse response to {}", responseType.getSimpleName(), e);
      throw new IOException("Failed to parse JSON response", e);
    }
  }

  public GetSessionResponse getSessionRequest(Map<String, String> headers, URI uri)
      throws IOException, InterruptedException {
    return sendGetRequest(headers, uri, GetSessionResponse.class);
  }

  public CustomerToBusinessTransactionResponse.SynchronousResponses
      customerToBusinessTransactionRequest(
          Map<String, String> headers, HttpRequest.BodyPublisher httpBody, URI uri)
          throws IOException, InterruptedException {
    return sendPostRequest(headers, httpBody, uri, CustomerToBusinessTransactionResponse.SynchronousResponses.class);
  }

  public BusinessToBusinessTransactionResponse.SynchronousResponse businessToBusinessTransactionRequest(
          Map<String, String> headers,
          HttpRequest.BodyPublisher httpBody,
          URI uri) throws IOException, InterruptedException {
    return sendPostRequest(headers, httpBody, uri, BusinessToBusinessTransactionResponse.SynchronousResponse.class);
  }
}
