package dev.korryr.mpesaapi.ui.componenets.mpesa.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.korryr.mpesaapi.ui.componenets.mpesa.model.PaymentState
import dev.korryr.mpesaapi.ui.componenets.mpesa.presentation.MpesaDialog
import dev.korryr.mpesaapi.ui.componenets.mpesa.presentation.ResultDialog
import dev.korryr.mpesaapi.ui.componenets.mpesa.viewModel.PaymentViewModel
import dev.korryr.mpesaapi.ui.theme.green99
import dev.korryr.mpesaapi.ui.theme.greenishA

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val paymentState by paymentViewModel.paymentState.collectAsState()
    var showMpesaDialog by remember { mutableStateOf(false)}
    var phoneNumber by remember { mutableStateOf("") }
    var showResultDialog by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }

    LaunchedEffect (paymentState) {
        when (paymentState) {
            is PaymentState.Success -> {
                val success = paymentState as PaymentState.Success
                resultMessage = "Paymert initiated Successfully!\n${success.message}\nTransaction ID: ${success.transactionId}"
                showResultDialog = true
                showMpesaDialog = false
            }
            is PaymentState.Error -> {
                val error = paymentState as PaymentState.Error
                resultMessage = "Payment failed: ${error.message}"
                showResultDialog = true
                //allow dialog open to allow retry
            }
            else -> {}
        }
    }

    if (showMpesaDialog){
        MpesaDialog(
            isProcessing = paymentState is PaymentState.Loading,
            onDismiss = {
                showMpesaDialog = false
                paymentViewModel.resetPaymentState()
            },
            onPayment = { amount ->
                paymentViewModel.initiatePaymnet(phoneNumber, amount)
                        },
            label = "Phone Number",
            number = phoneNumber,
            onNumberChange = { phoneNumber = it }

        )
    }

    if (showResultDialog) {
        ResultDialog(
            message = resultMessage,
            onDismiss = {
                showResultDialog = false
                paymentViewModel.resetPaymentState()
            }
        )
    }


    Scaffold(
        containerColor = greenishA,
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Use M-pesa Payment",
                        color = green99,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            )
        },
        content = { innerPadding ->
            Column (
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){

                Text(
                    text = "Click the button below to Pay",
                    color = green99,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier)

                Button(
                    onClick = {
                        showMpesaDialog = true
                    }
                ){
                    Text(
                        text = "M-Pesa",
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }
    )
}