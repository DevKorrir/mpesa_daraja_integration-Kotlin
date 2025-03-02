# mpesa_daraja_integration-Kotlin
Help to intergrade mobile payment through mpesa stkpush request for android apps

## M-Pesa Daraja API Integration

This application uses M-Pesa Daraja API for payment processing. To set up the application:

1. Register for M-Pesa Daraja API credentials at [Safaricom Developer Portal](https://developer.safaricom.co.ke/)
2. Copy `local.properties.template` to `local.properties` (which is git-ignored)
3. Fill in your M-Pesa API credentials in the `local.properties` file:
   ```properties
   mpesa.consumerKey=YOUR_CONSUMER_KEY_HERE
   mpesa.consumerSecret=YOUR_CONSUMER_SECRET_HERE
   mpesa.passkey=YOUR_PASSKEY_HERE
   mpesa.callbackUrl=YOUR_CALLBACK_URL_HERE
   mpesa.businessShortCode=174379
