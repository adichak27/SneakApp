package com.cs4520.assignment4

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cs4520.sneak.LoginScreen
import com.cs4520.sneak.MainActivity
import com.cs4520.sneak.PasswordReset
import com.cs4520.sneak.ProductListScreen
import com.cs4520.sneak.RegisterNewUserScreen
import com.cs4520.sneak.model.ProductViewModel
import com.cs4520.sneak.model.UserListViewModel
import com.google.common.truth.Truth
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterNewUserScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: NavHostController

    @Test
    fun registerNewUserScreenComponentsAreDisplayed() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            RegisterNewUserScreen(navController, vm = UserListViewModel(ApplicationProvider.getApplicationContext()))
        }
        // Check for Input Fields
        composeTestRule.onNodeWithTag("EmailInput").assertExists()
        composeTestRule.onNodeWithTag("UsernameInput").assertExists()
        composeTestRule.onNodeWithTag("PasswordInput").assertExists()


        // Check for Text
        composeTestRule.onNodeWithTag("LogoText").assertExists()
        composeTestRule.onNodeWithTag("CreateAccountText").assertExists()


        // Check for Buttons
        composeTestRule.onNodeWithTag("ConfirmButton").assertExists()
    }

    @Test
    fun resetPasswordScreenNavigatesToLoginScreen() {
        composeTestRule.setContent {
            navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                // Screen 1: Login
                composable("login") {
                    LoginScreen(navController)
                }
                // Screen 3: Register new user
                composable("newUser") {
                    RegisterNewUserScreen(navController)
                }
            }
        }

        composeTestRule.onNodeWithTag("CreateAccountButton").performClick()


        Truth.assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith("login")
        ).isTrue()

        // Enter username and password
        //composeTestRule.onNodeWithTag("EmailInput").performTextInput("test@testing.com")
        composeTestRule.onNodeWithTag("UsernameInput").performTextInput("testing")
        composeTestRule.onNodeWithTag("PasswordInput").performTextInput("123")

        // moving to Login screen
        //composeTestRule.onNodeWithTag("ConfirmButton").performClick()


        // check we are on the Login screen
        Truth.assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith("login")
        ).isTrue()
    }
}