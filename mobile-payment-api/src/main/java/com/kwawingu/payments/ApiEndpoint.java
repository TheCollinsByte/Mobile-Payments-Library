package com.kwawingu.payments;

public class ApiEndpoint {
    private final Environment environment;
    private final Market market;

    public ApiEndpoint(Environment environment, Market market) {
        this.environment = environment;
        this.market = market;
    }

    public String getUrl(Service service) {
        return (environment.isSsl() ? "https://" : "http://") +
                environment.getHost() + "/" +
                environment.getName().toLowerCase() +
                "/ipg/v2/" +
                market.getContextValue() +
                service.getPath();
    }
}
