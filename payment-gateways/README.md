# Gateway Implementation

> * Each package for a specific payment gateway, Gateway specific code.
> * This includes making API calls to the payment gateway's API, handling responses.
> * Each gateway implementation should implement the `PaymentGateway` interface defined in the `payment-core` module

#### Note: 
> Each gateway implementation includes robust error handling to gracefully manage various types of error that my arise during payment processing.