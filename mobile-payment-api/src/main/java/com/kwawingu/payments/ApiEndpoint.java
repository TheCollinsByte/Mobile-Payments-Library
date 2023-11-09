/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class ApiEndpoint {
  private final Environment environment;
  private final Market market;

  public ApiEndpoint(Environment environment, Market market) {
    this.environment = environment;
    this.market = market;
  }

  public URI getUrl(Service service) throws URISyntaxException {
    return new URI((environment.isSsl() ? "https://" : "http://")
        + environment.getHost()
        + "/"
        + environment.getName().toLowerCase(Locale.getDefault())
        + "/ipg/v2/"
        + market.getContextValue()
        + service.getPath());
  }
}
