package com.cs4520.sneak

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.cs4520.sneak.model.ProductViewModel
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.StateFlow




@Composable
fun CheckoutScreen(navController: NavHostController, viewModel: ProductViewModel) {
    // val cartItems by viewModel.cartItems.observeAsState(listOf())
    val cartItems = viewModel.cartItems.observeAsState(listOf()).value
    Log.d("CheckOutScreen", "length of cart = " + viewModel.cartItems.value?.size)


    Scaffold(
        topBar = { TopAppBar(title = { Text("Checkout") }) },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Text("Items In Your Cart:")
                LazyColumn(
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(cartItems) { itemName ->
                        Text(itemName)
                    }
                }
            }
        }
    )
}


