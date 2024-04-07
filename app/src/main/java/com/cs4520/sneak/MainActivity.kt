package com.cs4520.sneak
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import com.cs4520.sneak.R
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
       // val vm: ProductViewModel by viewModel()

        // Using ProcessLifecycleOwner to observe app lifecycle changes
        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                //viewModel.fetchLatestProductsImmediately()
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App title text
        Text(text = "$" + "neak",
            fontFamily = FontFamily.Serif,
            fontSize = 30.sp, // adjust the size
            modifier = Modifier.padding(bottom = 85.dp) // add space at the bottom
        )

        // Username TextField
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        // Add a spacer after the login input boxes
        Spacer(modifier = Modifier.height(20.dp))

        // Login Button
        Button(
            onClick = {
                // Logic for login
            }
        ) {
            Text("Login")
        }

        // Add a spacer after the login button
        Spacer(modifier = Modifier.height(10.dp))

        // Forgot Password Button
        Button(
            onClick = {
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text("Forgot Password?")
        }

        // Spacer to create additional space
        Spacer(modifier = Modifier.height(350.dp))

        // Create Account Button
        Button(
            onClick = {
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text("Create Account")
        }
    }
}

@Composable
fun PasswordReset(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App title text
        Text(text = "$" + "neak",
            fontFamily = FontFamily.Serif,
            fontSize = 30.sp, // adjust the size
            modifier = Modifier.padding(bottom = 85.dp) // add space at the bottom
        )

        Text(text = "Change Password:",
            fontFamily = FontFamily.Monospace,
            fontSize = 20.sp, // adjust the size
            modifier = Modifier.padding(bottom = 5.dp) // add space at the bottom
        )

        // Add a spacer after the login input boxes
        Spacer(modifier = Modifier.height(20.dp))

        // Username TextField
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("New Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Confirm New Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        // Add a spacer after the text boxes
        Spacer(modifier = Modifier.height(10.dp))

        // Confirm Button
        Button(
            onClick = {
                // Logic for changing password
            }
        ) {
            Text("Confirm")
        }
        Spacer(modifier = Modifier.height(100.dp))

    }
}

@Composable
fun RegisterNewUser(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App title text
        Text(text = "$" + "neak",
            fontFamily = FontFamily.Serif,
            fontSize = 30.sp, // adjust the size
            modifier = Modifier.padding(bottom = 85.dp) // add space at the bottom
        )

        // Add a spacer after the login input boxes
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Create Account:",
            fontFamily = FontFamily.Monospace,
            fontSize = 20.sp, // adjust the size
            modifier = Modifier.padding(bottom = 5.dp) // add space at the bottom
        )

        // Add a spacer after the login input boxes
        Spacer(modifier = Modifier.height(20.dp))


        // Password TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        // Username TextField
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        // Add a spacer after the text boxes
        Spacer(modifier = Modifier.height(10.dp))

        // Confirm Button
        Button(
            onClick = {
                // Logic for changing password
            }
        ) {
            Text("Register")
        }
        Spacer(modifier = Modifier.height(100.dp))

    }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RegisterNewUser(rememberNavController())
}