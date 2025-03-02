package dev.korryr.mpesaapi.ui.componenets.mpesa.model

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface MpesaApiService {
   //@Headers("Authorization: Bearer {token}")
   @GET("oauth/v1/generate?grant_type=client_credentials")
   suspend fun getAccessToken(@Header("Authorization") authorization: String): AccessTokenResponse

   @POST("mpesa/stkpush/v1/processrequest")
   suspend fun performStkPush(
       @Header("Authorization") authorization: String,
       @Body stkPushRequest: StkPushRequest
   ): StkPushResponse
}
