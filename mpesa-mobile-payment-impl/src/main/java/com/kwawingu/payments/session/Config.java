package com.kwawingu.payments.session;


public class Config {
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
            return new Config(this);
        }
    }
}
