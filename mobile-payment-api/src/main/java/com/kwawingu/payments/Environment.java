/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

public enum Environment {
  SANDBOX("Sandbox", true, "openapi.m-pesa.com", 443),
  OPENAPI("OpenAPI", true, "openapi.m-pesa.com", 443);

  private final String name;
  private final boolean ssl;
  private final String host;
  private final int port;

  Environment(String name, boolean ssl, String host, int port) {
    this.name = name;
    this.ssl = ssl;
    this.host = host;
    this.port = port;
  }

  public String getName() {
    return name;
  }

  public boolean isSsl() {
    return ssl;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }
}
