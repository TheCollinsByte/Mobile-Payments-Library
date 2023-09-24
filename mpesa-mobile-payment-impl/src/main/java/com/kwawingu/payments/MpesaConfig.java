package com.kwawingu.payments;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MpesaConfig implements MobilePayment {
    private static final Logger LOG = LoggerFactory.getLogger(MpesaConfig.class);

    private final String publicKey;
    private final String apiKey;
    private final HttpClient httpClient;

    public MpesaConfig(String publicKey, String apiKey) {
        this.publicKey = publicKey;
        this.apiKey = apiKey;
        httpClient = HttpClient.newHttpClient();
    }

    @Override
    public String generateAnEncryptApiKey() {

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);

        try {
            PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encryptedBytes = rsaCipher.doFinal(apiKey.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<String> getSessionKey(String encryptedApiKey, String context) throws IOException {
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

    private HttpRequest buildSessionRequest(String encryptedApiKey, String context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + encryptedApiKey);
        headers.put("Origin", "*");

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(context))
                .GET();

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
}