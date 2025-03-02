package dev.korryr.mpesaapi.ui.componenets.mpesa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.korryr.mpesaapi.ui.componenets.mpesa.model.MpesaRepository
import dev.korryr.mpesaapi.ui.componenets.mpesa.model.PaymentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val mpesaRepository: MpesaRepository
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()

    fun initiatePaymnet(phoneNumber: String, amount: String) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading

            if (!isValidPhoneNumber(phoneNumber)) {
                _paymentState.value = PaymentState.Error("Invalid phone number. Please enter a valid Safaricom number.")
                return@launch
            }

            if (!isValidAmount(amount)){
                _paymentState.value = PaymentState.Error("Please enter a valid amount greater than o.")
                return@launch
            }

            val result = mpesaRepository.performStkPush(phoneNumber, amount)
            result.fold(
                onSuccess = { response ->
                    if (response.ResponseCode == "0") {
                        _paymentState.value = PaymentState.Success(
                            message = response.CustomerMessage,
                            transactionId = response.CheckoutRequestID
                        )
                    } else {
                        _paymentState.value = PaymentState.Error(response.ResponseDescription)
                    }
                },

                onFailure = { error ->
                    _paymentState.value = PaymentState.Error(error.message ?: "Unknown error occurred")
                }
            )
        }
    }



    private fun isValidPhoneNumber(phone: String): Boolean {
        val cleaned = phone.replace(Regex("[^0-9]"), "")
        // Safaricom numbers in Kenya: Must be valid length and start with appropriate prefixes
        return when {
            cleaned.startsWith("254") -> cleaned.length ==12
            cleaned.startsWith("07") || cleaned.startsWith("01") -> cleaned.length == 10
            cleaned.startsWith("7") || cleaned.startsWith("1") -> cleaned.length == 9
            else -> false
        }
    }

    private fun isValidAmount(amount: String): Boolean {
        return try {
            val amountValue = amount.toDouble()
            amountValue > 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    // Reset the payment state
    fun resetPaymentState() {
        _paymentState.value = PaymentState.Idle
    }


}
