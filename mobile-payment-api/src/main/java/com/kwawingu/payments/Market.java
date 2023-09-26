package com.kwawingu.payments;

public enum Market {
    VODAFONE_GHANA("Vodafone Ghana", "vodafoneGHA", "GHA", "GHS"),
    VODACOM_TANZANIA("Vodacom Tanzania", "vodacomTZN", "TZN", "TZS"),
    VODACOM_LESOTHO("Vodacom Lesotho", "vodacomLES", "LES", "LSL"),
    VODACOM_DR_CONGO("Vodacom DR Congo", "vodacomDRC", "DRC", "USD");

    private final String description;
    private final String urlContextValue;
    private final String inputCountryValue;
    private final String inputCurrencyValue;

    Market(String description, String urlContextValue, String inputCountryValue, String inputCurrencyValue) {
        this.description = description;
        this.urlContextValue = urlContextValue;
        this.inputCountryValue = inputCountryValue;
        this.inputCurrencyValue = inputCurrencyValue;
    }

    public String getDescription() {
        return description;
    }

    public String getContextValue() {
        return urlContextValue;
    }

    public String getInputCountryValue() {
        return inputCountryValue;
    }

    public String getInputCurrencyValue() {
        return inputCurrencyValue;
    }
}
