package com.cs4520.sneak
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cs4520.sneak.data.UserRepository
import com.cs4520.sneak.data.database.SneakDB
import com.cs4520.sneak.model.ProductListViewModel
import com.cs4520.sneak.model.UserListViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmazingProductsApp()
        }

        // TODO: Do we need view model in MainActivity?
       //val vm: ProductListViewModel by viewModel()


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
fun LoginScreen(navController: NavHostController, vm: UserListViewModel = viewModel()) {
    // MutableState for username and password fields
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage by vm.error.collectAsState(null)
    val context = LocalContext.current
    // Observe changes to currentUser LiveData
    val currentUser by vm.currentUser.collectAsState()

    val userDao = SneakDB.getInstance(context).userDao()
    val repo = UserRepository(userDao)

    vm.initialize(repo)

    // Fetch the products when we launch this screen
    LaunchedEffect(Unit) {
        vm.fetchUsers()
    }

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
                vm.setCurrentUser(username, password)
                if (currentUser != null) {
                    if (username == currentUser!!.username && password == currentUser!!.password) {
                        // Display success login message
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        // Navigate to ProductListFragment
                        navController.navigate("productList")
                    } else {
                        // Display failed login message
                        Toast.makeText(context, "Invalid Login Info", Toast.LENGTH_LONG).show()
                    }
                }

//                if (errorMessage == null) {
//                    if (username == currentUser.username && password == currentUser.password) {
//                        // Display success login message
//                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
//                        // Navigate to ProductListFragment
//                        navController.navigate("productList")
//                    } else {
//                        // Display failed login message
//                        Toast.makeText(context, "Invalid Login Info", Toast.LENGTH_LONG).show()
//                    }
//                } else {
//                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                }

            }
        ) {
            Text("Login")
        }

        // Add a spacer after the login button
        Spacer(modifier = Modifier.height(10.dp))

        // Forgot Password Button
        Button(
            onClick = {
                      navController.navigate("resetPassword")
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
                      navController.navigate("newUser")
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
fun RegisterNewUser(navController: NavHostController, vm: UserListViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    val userDao = SneakDB.getInstance(context).userDao()
    val repo = UserRepository(userDao)

    vm.initialize(repo)

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


        // Email TextField
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
                vm.addUser(username, email, password)
                navController.navigate("login")
                Toast.makeText(context, "Successfully created account", Toast.LENGTH_LONG).show()

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