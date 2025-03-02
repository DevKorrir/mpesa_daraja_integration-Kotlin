package dev.korryr.mpesaapi.ui.componenets.mpesa.model

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class Success(val message: String, val transactionId: String) : PaymentState()
    data class Error(val message: String) : PaymentState()
}