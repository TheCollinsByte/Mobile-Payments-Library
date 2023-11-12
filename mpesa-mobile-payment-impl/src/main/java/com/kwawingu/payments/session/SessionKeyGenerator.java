/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.session;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kwawingu.payments.client.MpesaHttpClient;
import com.kwawingu.payments.client.response.GetSessionResponse;
import com.kwawingu.payments.exception.SessionKeyUnavailableException;
import com.kwawingu.payments.session.keys.MpesaEncryptedApiKey;
import com.kwawingu.payments.session.keys.MpesaSessionKey;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionKeyGenerator {

  private static final Logger LOG = LoggerFactory.getLogger(SessionKeyGenerator.class);

  private final MpesaHttpClient httpClient;

  public SessionKeyGenerator() {
    httpClient = new MpesaHttpClient();
  }

  private void handleSessionResponse(GetSessionResponse response) throws IOException {
    int statusCode = response.getHttpStatusCode();

    if (statusCode != 200 && statusCode != 400) {
      throw new IOException("Unexpected HTTP Code" + statusCode);
    }

    if (statusCode == 400) {
      throw new IOException("Session Creation Failed: " + statusCode);
    }
  }

  private MpesaSessionKey extractSessionKey(GetSessionResponse response) throws SessionKeyUnavailableException {
    if (response != null) {
      return response.getOutput_SessionID();
    }
    throw new SessionKeyUnavailableException("No session key available in JSON response.");
  }

  public MpesaSessionKey getSessionKeyOrThrowUnchecked(
      MpesaEncryptedApiKey encryptedApiKey, URI contextUri) {
    try {
      return getSessionKeyOrThrow(encryptedApiKey, contextUri);
    } catch (SessionKeyUnavailableException e) {
      LOG.error(
          "Error Processing session response: {}", e.getMessage() == null ? "" : e.getMessage());
      throw new IllegalStateException(e);
    }
  }

  public MpesaSessionKey getSessionKeyOrThrow(MpesaEncryptedApiKey encryptedApiKey, URI contextUri)
      throws SessionKeyUnavailableException {

    GetSessionResponse sessionResponse;

    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("Origin", "*");
    encryptedApiKey.insertAuthorizationHeader(headers);

    try {
      sessionResponse = httpClient.getSessionRequest(headers, contextUri);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }

    try {
      handleSessionResponse(sessionResponse);
      return extractSessionKey(sessionResponse);
    } catch (IOException e) {
      throw new SessionKeyUnavailableException(e);
    }
  }
}
