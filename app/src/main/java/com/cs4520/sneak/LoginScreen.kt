package com.cs4520.sneak

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cs4520.sneak.data.SneakApi
import com.cs4520.sneak.data.UserRepository
import com.cs4520.sneak.data.database.SneakDB
import com.cs4520.sneak.model.UserListViewModel


@Composable
fun LoginScreen(navController: NavHostController, vm: UserListViewModel = viewModel()) {
    // MutableState for username and password fields
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val errorMessage by vm.loginError.collectAsState(null)
    val context = LocalContext.current
    // Observe changes to currentUser LiveData
    val currentUser by vm.currentUser.collectAsState()

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