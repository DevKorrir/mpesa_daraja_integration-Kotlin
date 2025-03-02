package dev.korryr.mpesaapi.ui.componenets.mpesa.model

interface MpesaRepository {
    suspend fun getAccessToken(): Result<String>
    suspend fun performStkPush(phoneNumber: String, amount: String): Result<StkPushResponse>
}