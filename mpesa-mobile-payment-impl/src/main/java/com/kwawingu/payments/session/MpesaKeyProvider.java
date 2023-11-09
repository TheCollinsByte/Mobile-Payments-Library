/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments.session;

import com.kwawingu.payments.session.keys.MpesaApiKey;
import com.kwawingu.payments.session.keys.MpesaPublicKey;

public interface MpesaKeyProvider {
  MpesaApiKey getApiKey();

  MpesaPublicKey getPublicKey();
}
