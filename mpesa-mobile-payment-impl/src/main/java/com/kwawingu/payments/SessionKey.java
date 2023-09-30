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
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionKey {

  private static final Logger LOG = LoggerFactory.getLogger(SessionKey.class);

  private final HttpClient httpClient;

  public SessionKey() {
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

  private Optional<String> extractSessionKey(String responseBody) {
    if (responseBody != null) {
      JsonElement jsonElement = JsonParser.parseString(responseBody);

      if (jsonElement.isJsonObject()) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.has("output_SessionID")) {
          return Optional.of(jsonObject.get("output_SessionID").getAsString());
        }
      }
    }
    return Optional.empty();
  }

  public Optional<String> getSessionKey(String encryptedApiKey, String context) {
    HttpResponse<String> response;

    HttpRequest request = buildSessionRequest(encryptedApiKey, context);
    response = sendSessionRequest(request);

    try {
      handleSessionResponse(response);
      return extractSessionKey(response.body());
    } catch (IOException e) {
      LOG.debug("Error Processing session response: {}", e.getMessage());
    }

    return Optional.empty();
  }
}
