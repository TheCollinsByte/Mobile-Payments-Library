/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.session;

import com.kwawingu.payments.c2b.CustomerToBusinessTransaction;
import com.kwawingu.payments.session.keys.MpesaApiKey;
import com.kwawingu.payments.session.keys.MpesaPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MpesaKeyProviderFromEnvironment implements MpesaKeyProvider {
  @SuppressWarnings("UnusedVariable")
  private static final Logger LOG = LoggerFactory.getLogger(CustomerToBusinessTransaction.class);

  public static class Config {
    private final String apiKeyEnvName;
    private final String publicKeyEnvName;

    private Config(Builder builder) {
      this.apiKeyEnvName = builder.apiKeyEnvName;
      this.publicKeyEnvName = builder.publicKeyEnvName;
    }

    public String getApiKeyEnvName() {
      return apiKeyEnvName;
    }

    public String getPublicKeyEnvName() {
      return publicKeyEnvName;
    }

    @SuppressWarnings("initialization.field.uninitialized")
    public static class Builder {
      private String apiKeyEnvName;
      private String publicKeyEnvName;

      public Builder setApiKeyEnvName(String apiKeyEnvName) {
        this.apiKeyEnvName = apiKeyEnvName;
        return this;
      }

      public Builder setPublicKeyEnvName(String publicKeyEnvName) {
        this.publicKeyEnvName = publicKeyEnvName;
        return this;
      }

      public Config build() {
        Objects.requireNonNull(apiKeyEnvName, "API Key environment name cannot be null");
        Objects.requireNonNull(publicKeyEnvName, "Public Key environment name cannot be null");

        return new Config(this);
      }
    }
  }

  private final Config config;

  public MpesaKeyProviderFromEnvironment(Config config) {
    this.config = config;
  }

  @Override
  public MpesaApiKey getApiKey() {
    String apiKey = System.getenv(config.getApiKeyEnvName());
    if (apiKey == null || apiKey.trim().isEmpty()) {
      throw new IllegalStateException(
          "Your Did not provide the API Key in " + config.getApiKeyEnvName());
    }
    return new MpesaApiKey(apiKey);
  }

  @Override
  public MpesaPublicKey getPublicKey() {
    String publicKey = System.getenv(config.getPublicKeyEnvName());
    if (publicKey == null || publicKey.trim().isEmpty()) {
      throw new IllegalStateException(
          "You did not provide the API Key in " + config.getPublicKeyEnvName());
    }
    return new MpesaPublicKey(publicKey);
  }
}
