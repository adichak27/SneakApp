package com.cs4520.sneak
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.cs4520.sneak.data.SneakApi
import com.cs4520.sneak.data.UserRepository
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.data.database.SneakDB
import com.cs4520.sneak.model.ProductViewModel
import com.cs4520.sneak.model.ShoeUiState
import com.cs4520.sneak.model.UserListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmazingProductsApp()
        }

        // TODO: Do we need view model in MainActivity?
        val viewModel: ProductViewModel by viewModels()

        // Using ProcessLifecycleOwner to observe app lifecycle changes
        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.fetchLatestShoesImmediately()
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
        // Screen 6: Thanks Screen
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
    val errorMessage by vm.loginError.collectAsState(null)
    val context = LocalContext.current
    // Observe changes to currentUser LiveData
    val currentUser by vm.currentUser.collectAsState()

    val userDao = SneakDB.getInstance(context).userDao()
    val repo = UserRepository(SneakApi.apiService)

    vm.initialize(repo)

    // Fetch the products when we launch this screen
    LaunchedEffect(Unit) {
        vm.fetchUsers()
        println("users: " + vm.fetchUsers())
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
                vm.setCurrentUser(username)
                if (errorMessage == null) {
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
                    } else {
                        Toast.makeText(context, "Invalid Login Info", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
                // Clears login error value after an attempt
                vm.clearLoginError()
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
fun PasswordReset(navController: NavHostController, vm: UserListViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    val errorMessage by vm.resetPasswordError.collectAsState(null)
    val context = LocalContext.current

    val userDao = SneakDB.getInstance(context).userDao()
    val repo = UserRepository(SneakApi.apiService)

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
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        // Password TextField
        TextField(
            value = confirmNewPassword,
            onValueChange = { confirmNewPassword = it },
            label = { Text("Confirm New Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )

        // Add a spacer after the text boxes
        Spacer(modifier = Modifier.height(10.dp))

        // Confirm Button
        Button(
            onClick = {
               if (newPassword != confirmNewPassword) {
                   Toast.makeText(context, "Passwords Don't Match", Toast.LENGTH_LONG).show()
               } else {
                   vm.editUser(username, newPassword)
                   if (errorMessage == null) {
                       navController.navigate("login")
                       Toast.makeText(context, "Successfully Changed Password", Toast.LENGTH_LONG).show()
                   } else {
                       Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                   }
               }
                vm.clearResetPasswordError()
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
    val repo = UserRepository(SneakApi.apiService)

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
fun ProductListScreen(navController: NavHostController, viewModel: ProductViewModel = viewModel()) {
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

@Composable
fun ProductItem(shoe: Shoe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            modifier = Modifier
                .fillMaxWidth()
                .background(color = when (shoe.type) {
                    "Adidas" -> colorResource(id = R.color.adidas_color)
                    "Converse" -> colorResource(id = R.color.converse_color)
                    "Nike" -> colorResource(id = R.color.nike_color)
                    "ASICS" -> colorResource(id = R.color.asics_color)
                    "Vans" -> colorResource(id = R.color.vans_color)
                    "Reebok" -> colorResource(id = R.color.reebok_color)
                    else -> colorResource(id = R.color.default_shoe_color) // Default color if none match
                })
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(
                    id = when (shoe.type) {
                        "Adidas" -> R.drawable.adidas
                        "Converse" -> R.drawable.converse
                        "Nike" -> R.drawable.nike
                        "ASICS" -> R.drawable.asics
                        "Vans" -> R.drawable.vans
                        "Reebok" -> R.drawable.reebok
                        else -> R.drawable.equipment // Default image if none match
                    }
                ),
                contentDescription = "Shoe Image",
                modifier = Modifier
                    .size(60.dp) // Ensures the image is always 60dp x 60dp
                    .clip(CircleShape), // Optional: Clips the image to a circle
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = shoe.name, style = MaterialTheme.typography.h6)
                Text(text = "Manufacturer: ${shoe.manufacturer}", style = MaterialTheme.typography.body1)
                Text(text = "Type: ${shoe.type}", style = MaterialTheme.typography.body2)
                Text(text = "Price: $${shoe.price}", style = MaterialTheme.typography.body2)
            }
        }
    }
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