package com.cs4520.sneak

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cs4520.sneak.model.ProductViewModel

@Composable
fun ThanksScreen(navController: NavHostController, viewModel: ProductViewModel) {
    val cartItems = viewModel.cartItems.observeAsState(listOf()).value
    val numProducts = cartItems.size

    Scaffold(
        topBar = { TopAppBar(title = { Text("Thank You") }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Thanks for your purchase!", style = MaterialTheme.typography.h4)
                Text("Your products will arrive in approximately $numProducts days.", style = MaterialTheme.typography.subtitle1)
            }
        }
    )
}