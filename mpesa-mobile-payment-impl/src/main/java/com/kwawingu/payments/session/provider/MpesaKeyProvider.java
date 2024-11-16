/*
 * Copyright 2021-2024 KwaWingu.
 */
package com.kwawingu.payments.session.provider;

import com.kwawingu.payments.session.keys.MpesaApiKey;
import com.kwawingu.payments.session.keys.MpesaPublicKey;

public interface MpesaKeyProvider {
  MpesaApiKey getApiKey();

  MpesaPublicKey getPublicKey();
}
