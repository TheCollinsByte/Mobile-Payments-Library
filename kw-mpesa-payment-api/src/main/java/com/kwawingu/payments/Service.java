/*
 * Copyright 2021-2024 KwaWingu.
 */
package com.kwawingu.payments;

public enum Service {
  GET_SESSION("/getSession/"),
  CUSTOMER_TO_BUSINESS("/c2bPayment/singleStage/"),
  BUSINESS_TO_BUSINESS("/b2bPayment/");

  private final String service;

  Service(String service) {
    this.service = service;
  }

  public String getPath() {
    return service;
  }
}
