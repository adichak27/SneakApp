package com.cs4520.sneak
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cs4520.sneak.model.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmazingProductsApp()
        }
    }
}

@Composable
fun AmazingProductsApp() {
    val navController = rememberNavController()
    val viewModel: ProductViewModel = viewModel()
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
            RegisterNewUserScreen(navController)
        }
        // Screen 4: Product List
        composable("productList") {
            ProductListScreen(navController, viewModel)
        }
        // Screen 5: Checkout Screen
        composable("checkout") {
            CheckoutScreen(navController, viewModel)
        }
        // Screen 6: Thanks Screen
        composable("thanks") {
            ThanksScreen(navController)
        }
    }
}

@Composable
fun ThanksScreen(navController: NavHostController) {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RegisterNewUserScreen(rememberNavController())
}