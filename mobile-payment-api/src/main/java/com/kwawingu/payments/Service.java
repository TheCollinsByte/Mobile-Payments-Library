/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

public enum Service {
  GET_SESSION("/getSession/");

  private final String path;

  Service(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
