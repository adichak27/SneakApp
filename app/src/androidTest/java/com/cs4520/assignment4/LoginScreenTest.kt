package com.cs4520.assignment4

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cs4520.sneak.AmazingProductsApp
import com.cs4520.sneak.CheckoutScreen
import com.cs4520.sneak.LoginScreen
import com.cs4520.sneak.MainActivity
import com.cs4520.sneak.PasswordReset
import com.cs4520.sneak.ProductListScreen
import com.cs4520.sneak.RegisterNewUserScreen
import com.cs4520.sneak.ThanksScreen
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.model.ProductViewModel
import com.cs4520.sneak.model.UserListViewModel
import com.google.common.truth.Truth.assertThat
import okhttp3.Route
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: NavHostController

    @Test
    fun loginScreenComponentsAreDisplayed() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController, vm = UserListViewModel(ApplicationProvider.getApplicationContext()))
        }
        // Check for Input Fields
        composeTestRule.onNodeWithTag("UsernameInput").assertExists()
        composeTestRule.onNodeWithTag("PasswordInput").assertExists()

        // Check for Logo Text
        composeTestRule.onNodeWithTag("LogoText").assertExists()

        // Check for Buttons
        composeTestRule.onNodeWithTag("LoginButton").assertExists()
        composeTestRule.onNodeWithTag("ForgotPasswordButton").assertExists()
        composeTestRule.onNodeWithTag("CreateAccountButton").assertExists()
    }

    @Test
    fun loginScreenNavigatesToProductScreen() {
        composeTestRule.setContent {
            navController = rememberNavController()
            val viewModel: ProductViewModel = viewModel()
            NavHost(navController = navController, startDestination = "login") {
                // Screen 1: Login
                composable("login") {
                    LoginScreen(navController)
                }
                // Screen 4: Product List
                composable("productList") {
                    ProductListScreen(navController, viewModel)
                }
                composable("resetPassword") {
                    PasswordReset(navController)
                }
            }
        }

        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith("login")
        ).isTrue()

        // Enter username and password
        composeTestRule.onNodeWithTag("UsernameInput").performTextInput("admin")
        composeTestRule.onNodeWithTag("PasswordInput").performTextInput("admin")

        // moving to Product screen
        composeTestRule.onNodeWithTag("LoginButton").performClick()


        // check we are on the Product screen
        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith("resetPassword")
        ).isTrue()
    }
}
