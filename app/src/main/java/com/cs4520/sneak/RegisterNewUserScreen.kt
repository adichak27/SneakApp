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
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun RegisterNewUserScreen(navController: NavHostController, vm: UserListViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    val repo = UserRepository(SneakApi.apiService)

    vm.initialize(repo)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App title text
        Text(
            text = "$" + "neak",
            fontFamily = FontFamily.Serif,
            fontSize = 30.sp, // adjust the size
            modifier = Modifier.padding(bottom = 85.dp) // add space at the bottom
        )

        // Add a spacer after the login input boxes
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Create Account:",
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