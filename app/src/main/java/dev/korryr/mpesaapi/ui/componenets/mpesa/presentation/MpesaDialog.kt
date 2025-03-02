package dev.korryr.mpesaapi.ui.componenets.mpesa.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.korryr.mpesaapi.ui.theme.green99

@Composable
fun MpesaDialog(
    label: String,
    number: String = "",
    isProcessing: Boolean = false,
    onDismiss: () -> Unit = {},
    onPayment: (String) -> Unit = {},
    onNumberChange: (String) -> Unit = {},

){
    var amount by rememberSaveable { mutableStateOf("") }

    val rainbowColors = listOf(Cyan, Blue, Green , Red/*...*/)

    val brush = remember {
        Brush.linearGradient(
            colors = rainbowColors
        )
    }

    Dialog(
        onDismissRequest = onDismiss
    ){
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Enter Safaricom number",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = number,
                    onValueChange = onNumberChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    label = { Text(
                        text = label
                    )
                    },
                    placeholder = { Text("254712345678") },
                    textStyle = androidx.compose.ui.text.TextStyle(brush = brush)

                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text (
                            text = "Enter Amount"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    TextButton(
                        onClick = onDismiss,
                        enabled = !isProcessing
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Red
                        )
                    }
                    Button(
                        onClick = {
                            onPayment(amount)
                        },
                        enabled = number.isNotBlank() && !isProcessing && amount.isNotBlank()
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = green99
                            )
                        } else {
                            Text("Pay Now")
                        }
                    }
                }
            }
        }
    }
}