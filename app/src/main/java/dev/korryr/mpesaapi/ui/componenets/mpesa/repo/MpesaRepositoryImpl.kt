package dev.korryr.mpesaapi.ui.componenets.mpesa.repo

import android.content.Context
import android.util.Base64
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.korryr.mpesaapi.BuildConfig
import dev.korryr.mpesaapi.ui.componenets.mpesa.model.MpesaApiService
import dev.korryr.mpesaapi.ui.componenets.mpesa.model.MpesaRepository
import dev.korryr.mpesaapi.ui.componenets.mpesa.model.StkPushRequest
import dev.korryr.mpesaapi.ui.componenets.mpesa.model.StkPushResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MpesaRepositoryImpl @Inject constructor(
    private val mpesaApiService: MpesaApiService,
    @ApplicationContext private val context: Context
) : MpesaRepository {

    companion object {
        private const val CONSUMER_KEY = BuildConfig.CONSUMER_KEY
        private const val CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET
        private const val BUSINESS_SHORT_CODE = BuildConfig.BUSINESS_SHORT_CODE
        private const val PASSKEY = BuildConfig.PASSKEY
        private const val TRANSACTION_TYPE = "CustomerPayBillOnline"
        private const val CALLBACK_URL = BuildConfig.CALLBACK_URL
        private const val ACCOUNT_REFERENCE = "MpesaTest"
        private const val TRANSACTION_DESC = "Payment of Order"
    }

    private val base64Credentials: String by lazy {
        val credentials = "$CONSUMER_KEY:$CONSUMER_SECRET"
        Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
    }

    override suspend fun getAccessToken(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = mpesaApiService.getAccessToken("Basic $base64Credentials")
            Result.success(response.access_token)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun performStkPush(phoneNumber: String, amount: String): Result<StkPushResponse> = withContext(Dispatchers.IO) {
        try {
            // First get access token
            val tokenResult = getAccessToken()
            if (tokenResult.isFailure) {
                Log.e("MpesaRepository", "Failed to get access token", tokenResult.exceptionOrNull())
                return@withContext Result.failure(tokenResult.exceptionOrNull() ?: Exception("Failed to get access token"))
            }

            val accessToken = tokenResult.getOrNull() ?: return@withContext Result.failure(Exception("Access token is null"))

            // Format phone number (remove leading 0 if present and ensure it starts with 254)
            val formattedPhone = formatPhoneNumber(phoneNumber)
            Log.d("MpesaRepository", "Formatted phone: $formattedPhone")

            // Generate timestamp
            val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

            // Generate password (Shortcode + Passkey + Timestamp)
            val password = Base64.encodeToString("$BUSINESS_SHORT_CODE$PASSKEY$timestamp".toByteArray(), Base64.NO_WRAP)

            // Format amount to ensure it's a valid integer with no decimals
            val formattedAmount = amount.toDoubleOrNull()?.toInt()?.toString() ?:
            throw IllegalArgumentException("Invalid amount: $amount")

            Log.d("MpesaRepository", "STK Push request - Phone: $formattedPhone, Amount: $formattedAmount, Timestamp: $timestamp")

            // Create STK Push request
            val stkRequest = StkPushRequest(
                BusinessShortCode = BUSINESS_SHORT_CODE,
                Password = password,
                Timestamp = timestamp,
                TransactionType = TRANSACTION_TYPE,
                Amount = formattedAmount,
                PartyA = formattedPhone,
                PartyB = BUSINESS_SHORT_CODE,
                PhoneNumber = formattedPhone,
                CallBackURL = CALLBACK_URL,
                AccountReference = ACCOUNT_REFERENCE,
                TransactionDesc = TRANSACTION_DESC
            )

            try {
                // Perform the STK Push request
                val response = mpesaApiService.performStkPush("Bearer $accessToken", stkRequest)
                Log.d("MpesaRepository", "STK Push successful: ${response.ResponseDescription}")
                Result.success(response)
            } catch (e: HttpException) {
                // Extract error details from Retrofit HttpException
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("MpesaRepository", "HTTP Error: ${e.code()}, Body: $errorBody", e)
                Result.failure(Exception("HTTP ${e.code()}: ${errorBody ?: e.message()}"))
            }
        } catch (e: Exception) {
            Log.e("MpesaRepository", "STK Push failed", e)
            Result.failure(e)
        }
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        // Remove any spaces, dashes, or other non-digit characters
        var cleaned = phoneNumber.replace(Regex("[^0-9]"), "")

        // Remove leading zero if present
        if (cleaned.startsWith("0")) {
            cleaned = cleaned.substring(1)
        }
        // Ensure the phone number starts with 254
        if (!cleaned.startsWith("254")) {
            cleaned = "254$cleaned"
        }
        return cleaned
    }




}