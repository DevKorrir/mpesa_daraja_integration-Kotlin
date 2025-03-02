package dev.korryr.mpesaapi.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.korryr.mpesaapi.ui.componenets.mpesa.view.PaymentScreen
import dev.korryr.mpesaapi.ui.theme.MpesaApiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            MpesaApiTheme {
                PaymentScreen(
                    modifier = Modifier
                )
            }
        }
    }
}




