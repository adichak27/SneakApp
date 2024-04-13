package com.cs4520.sneak

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.cs4520.sneak.ProductItem
import com.cs4520.sneak.model.ProductViewModel
import com.cs4520.sneak.model.ShoeUiState


@Composable
fun ProductListScreen(navController: NavHostController, viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    // val products by viewModel.products.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Products") }) },
        content = { padding ->
            ProductList(uiState, padding)
        }
    )
}


@Composable
fun ProductList(uiState: ShoeUiState, padding: PaddingValues) {
    when (uiState) {
        is ShoeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Show loading indicator
            }
        }
        is ShoeUiState.Success -> {
            if (uiState.shoes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No products found")
                    // this should never execute
                }
            } else {
                LazyColumn(contentPadding = padding) {
                    items(uiState.shoes) { product ->
                        ProductItem(product)
                    }
                }
            }
        }
        is ShoeUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.exception.localizedMessage}") // Show error message
            }
        }
    }
}