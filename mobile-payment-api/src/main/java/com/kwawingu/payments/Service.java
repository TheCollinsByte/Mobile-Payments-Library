/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

public enum Service {
  GET_SESSION("/getSession/"),
  CUSTOMER_TO_BUSINESS("/c2bPayment/singleStage/");

  private final String service;

  Service(String service) {
    this.service = service;
  }

  public String getPath() {
    return service;
  }
}
