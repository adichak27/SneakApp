package com.cs4520.assignment4
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmazingProductsApp()
        }

        // TODO: Do we need view model in MainActivity?
        val vm: ProductViewModel by viewModel()

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
        // Screen 1: Login
        composable("login") {
            LoginScreen(navController)
        }
        // Screen 2: Reset password
        composable("resetPassword") {
            PasswordReset(navController)
        }
        // Screen 3: Register new user
        composable("newUser") {
            RegisterNewUser(navController)
        }
        // Screen 4: Product List
        composable("productList") {
            ProductListScreen(navController)
        }
        // Screen 5: Checkout Screen
        composable("checkout") {
            CheckoutScreen(navController)
        }
        // Screen 6: Checkout Screen
        composable("thanks") {
            ThanksScreen(navController)
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    // MutableState for username and password fields
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
}

@Composable
fun PasswordReset(navController: NavHostController) {

}

@Composable
fun RegisterNewUser(navController: NavHostController) {

}

@Composable
fun ProductListScreen(navController: NavHostController, vm: ProductListViewModel = viewModel()) {

}

@Composable
fun CheckoutScreen(navController: NavHostController) {

}

@Composable
fun ThanksScreen(navController: NavHostController) {

}