/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.kwawingu.payments.exception.SessionKeyUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionKeyGenerator {

  private static final Logger LOG = LoggerFactory.getLogger(SessionKeyGenerator.class);

  private final HttpClient httpClient;

  public SessionKeyGenerator() {
    httpClient = HttpClient.newHttpClient();
  }

  private HttpRequest buildSessionRequest(String encryptedApiKey, String context) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Authorization", "Bearer " + encryptedApiKey);
    headers.put("Origin", "*");

    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(context)).GET();

    headers.forEach(requestBuilder::headers);

    return requestBuilder.build();
  }

  private HttpResponse<String> sendSessionRequest(HttpRequest request) {
    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void handleSessionResponse(HttpResponse<String> response) throws IOException {
    int statusCode = response.statusCode();

    if (statusCode != 200 && statusCode != 400) {
      throw new IOException("Unexpected HTTP Code" + statusCode);
    }

    if (statusCode == 400) {
      throw new IOException("Session Creation Failed: " + statusCode);
    }
  }

  private String extractSessionKey(String responseBody) throws SessionKeyUnavailableException {
    if (responseBody != null) {
      JsonElement jsonElement = JsonParser.parseString(responseBody);

      if (jsonElement.isJsonObject()) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.has("output_SessionID")) {
          return jsonObject.get("output_SessionID").getAsString();
        }
      }
    }
    throw new SessionKeyUnavailableException("No session key available in JSON response.");
  }

  public String getSessionKeyOrThrowUnchecked(String encryptedApiKey, String context) {
    try {
      return getSessionKeyOrThrow(encryptedApiKey, context);
    } catch (SessionKeyUnavailableException e) {
      LOG.debug("Error Processing session response: {}", e.getMessage() == null ? "" : e.getMessage());
      throw new IllegalStateException(e);
    }
  }

  public String getSessionKeyOrThrow(String encryptedApiKey, String context) throws SessionKeyUnavailableException {
    HttpResponse<String> response;

    HttpRequest request = buildSessionRequest(encryptedApiKey, context);
    response = sendSessionRequest(request);

    try {
      handleSessionResponse(response);
      return extractSessionKey(response.body());
    } catch (IOException e) {
      throw new SessionKeyUnavailableException(e);
    }
  }
}
