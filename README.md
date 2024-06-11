<div align="center">

<h1><a href="https://github.com/TheOddagen/Mobile-Payments-Library">Mobile Payment Library</a></h1>

<a href="https://github.com/TheOddagen/Mobile-Payments-Library/graphs/contributors">
<img alt="People" src="https://img.shields.io/github/contributors/TheOddagen/Mobile-Payments-Library?style=flat&color=ffaaf2&label=People"> </a>

<a href="https://github.com/TheOddagen/Mobile-Payments-Library/stargazers">
<img alt="Stars" src="https://img.shields.io/github/stars/TheOddagen/Mobile-Payments-Library?style=flat&color=98c379&label=Stars"> </a>

<a href="https://github.com/TheOddagen/Mobile-Payments-Library/network/members">
<img alt="Forks" src="https://img.shields.io/github/forks/TheOddagen/Mobile-Payments-Library?style=flat&color=66a8e0&label=Forks"> </a>

<a href="https://github.com/TheOddagen/Mobile-Payments-Library/watchers">
<img alt="Watches" src="https://img.shields.io/github/watchers/TheOddagen/Mobile-Payments-Library?style=flat&color=f5d08b&label=Watches"> </a>

<a href="https://github.com/TheOddagen/Mobile-Payments-Library/pulse">
<img alt="Last Updated" src="https://img.shields.io/github/last-commit/TheOddagen/Mobile-Payments-Library?style=flat&color=e06c75&label="> </a>

</div>


## Description

This is open-source mobile payment library with support for M-Pesa, Tigo Pesa, Airtel Money and Halopesa. It provides a simple and intuitive API for integrating mobile payments into your applications.

- [Installation](#installation)
- [Features](#features)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)


## Features
- Easy integration with Vodacom (M-Pesa), Tigo (Tigo Pesa), Airtel (Airtel Money), PesaPal, SelcomPay, AzamPay and Halotel (HaloPesa)
- Support to various payment methods
- Extensible for other mobile payment providers
- Detailed error handling and logging

## Installation

### Gradle

Add the following dependency to your `build.gradle` file:

```groovy
dependencies {
    implementation 'com.kwawingu:mobile-payment:1.0.0'
}
```

### Maven

Add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.kwawingu</groupId>
    <artifactId>mobile-payment</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### Initialization

First, initialize the library with your M-Pesa credentials from environment variables.

```java
MpesaKeyProviderFromEnvironment.Config config =
        new MpesaKeyProviderFromEnvironment.Config.Builder()
                .setApiKeyEnvName("MPESA_API_KEY")
                .setPublicKeyEnvName("MPESA_PUBLIC_KEY")
                .build();
mpesaSessionKeyGenerator = new SessionKeyGenerator();
apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
keyProvider = new MpesaKeyProviderFromEnvironment(config);
```

This configuration sets up the MpesaKeyProviderFromEnvironment to retrieve the API key and public key from the specified environment variables. The SessionKeyGenerator and ApiEndpoint are also initialized for generating session keys and defining the API endpoint, respectively.

### Customer To Business (C2B)

The C2B API call is used as a standard customer-to-business transaction. Funds from the customer’s mobile money wallet will be deducted and be transferred to the mobile money wallet of the business. To authenticate and authorize this transaction, M-Pesa Payments Gateway will initiate a USSD Push message to the customer to gather and verify the mobile money PIN number. This number is not stored and is used only to authorize the transaction.

```java
Payload payload =
        new Payload.Builder()
                .setAmount("10.00")
                .setCustomerMSISDN("+255-762-578-467")
                .setCountry(Market.VODACOM_TANZANIA.getInputCountryValue())
                .setCurrency(Market.VODACOM_TANZANIA.getInputCurrencyValue())
                .setServiceProviderCode("ORG001")
                .setTransactionReference("T12344C")
                .setThirdPartyConversationID("1e9b774d1da34af78412a498cbc28f5e")
                .setPurchasedItemsDesc("Lenovo ThinkPad X1 Carbon Gen 12")
                .build();

CustomerToBusinessTransaction customerToBusinessTransaction =
        new CustomerToBusinessTransaction.Builder()
                .setApiEndpoint(new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA))
                .setEncryptedSessionKey(session.getEncryptedSessionKey())
                .setPayload(payload)
                .build();
```


## Contributing

Contributions are welcome! Please read the [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on how to contribute to this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

Collin - [collo@fastmail.com](mailto:collo@fastmail.com)

Project Link: [https://github.com/TheOddagen/Mobile-Payments-Library](https://github.com/TheOddagen/Mobile-Payments-Library)
