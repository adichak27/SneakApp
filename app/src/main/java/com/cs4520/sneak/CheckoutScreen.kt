package com.cs4520.sneak

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.cs4520.sneak.model.ProductViewModel
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cs4520.sneak.data.database.Shoe
import kotlinx.coroutines.flow.StateFlow


@Composable
fun CheckoutScreen(navController: NavHostController, viewModel: ProductViewModel) {
    val cartItems = viewModel.cartItems.observeAsState(listOf()).value
    val context = LocalContext.current


    var cardNumber by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val subtotal = cartItems.sumOf { it.price }
    val tax = subtotal * 0.06
    val total = subtotal + tax

    Scaffold(
        topBar = { TopAppBar(title = { Text("Checkout") }) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                item {
                    Text("Items In Your Cart:", style = MaterialTheme.typography.h6)
                    Spacer(Modifier.height(8.dp))
                }
                items(cartItems) { shoe ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(shoe.name, style = MaterialTheme.typography.body1)
                        Text("${shoe.price}", style = MaterialTheme.typography.body1)
                    }
                }
                item {
                    Spacer(Modifier.height(10.dp))
                    Text("Subtotal: $${"%.2f".format(subtotal)}", style = MaterialTheme.typography.subtitle1)
                    Text("Tax (6%): $${"%.2f".format(tax)}", style = MaterialTheme.typography.subtitle1)
                    Text("Total: $${"%.2f".format(total)}", style = MaterialTheme.typography.subtitle1)
                    Spacer(Modifier.height(10.dp))
                    // Credit Card Input Form
                    CreditCardForm(cardNumber, expirationDate, cvv) {
                        cardNumber = it.first
                        expirationDate = it.second
                        cvv = it.third
                    }
                    // Purchase Button
                    Button(
                        onClick = {
                            val validation = validateCreditCardInput(cardNumber, expirationDate, cvv)
                            if (validation.first) {
                                navController.navigate("thanks")
                            } else {
                                Toast.makeText(context, validation.second, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Purchase")
                    }

                }
            }
        }
    )
}

@Composable
fun CreditCardForm(cardNumber: String, expirationDate: String, cvv: String, onValueChange: (Triple<String, String, String>) -> Unit) {
    TextField(
        value = cardNumber,
        onValueChange = { onValueChange(Triple(it, expirationDate, cvv)) },
        label = { Text("Card Number") },
        placeholder = { Text("Enter 10 digits") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = expirationDate,
        onValueChange = { onValueChange(Triple(cardNumber, it, cvv)) },
        label = { Text("Expiration Date (MM/YY)") },
        placeholder = { Text("MM/YY") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = cvv,
        onValueChange = { onValueChange(Triple(cardNumber, expirationDate, it)) },
        label = { Text("CVV") },
        placeholder = { Text("3 digits") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
fun validateCreditCardInput(cardNumber: String, expirationDate: String, cvv: String): Pair<Boolean, String> {
    if (cardNumber.length != 10) {
        return Pair(false, "Invalid card number: must be 10 digits")
    }
    if (!cardNumber.all { it.isDigit() }) {
        return Pair(false, "Invalid card number: must contain only digits")
    }
    if (!expirationDate.matches(Regex("\\d{2}/\\d{2}"))) {
        return Pair(false, "Invalid expiration date: must be MM/YY")
    }

    val (month, year) = expirationDate.split("/").map { it.toInt() }
    if (month !in 1..12) {
        return Pair(false, "Invalid month in expiration date: must be between 01 and 12")
    }
    if (year <= 23) {
        return Pair(false, "Invalid year in expiration date: must be greater than 23")
    }

    if (cvv.length != 3 || !cvv.all { it.isDigit() }) {
        return Pair(false, "Invalid CVV: must be 3 digits")
    }
    return Pair(true, "")
}
