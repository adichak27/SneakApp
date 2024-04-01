package com.cs4520.assignment4
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import com.cs4520.assignment5.ProductListScreen
import com.cs4520.assignment5.R
import androidx.lifecycle.ProcessLifecycleOwner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmazingProductsApp()
        }
        val viewModel: ProductViewModel by viewModels()

        // Using ProcessLifecycleOwner to observe app lifecycle changes
        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.fetchLatestProductsImmediately()
            }
        })
    }
}

@Composable
fun AmazingProductsApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("productList") {
            ProductListScreen(navController)
        }
    }
}