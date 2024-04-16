package com.cs4520.assignment4

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cs4520.sneak.CheckoutScreen
import com.cs4520.sneak.LoginScreen
import com.cs4520.sneak.MainActivity
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.model.ProductViewModel
import com.cs4520.sneak.model.UserListViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginScreenComponentsAreDisplayed() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(navController, vm = UserListViewModel(ApplicationProvider.getApplicationContext()))
        }
        // Check for Input Fields
        composeTestRule.onNodeWithTag("UsernameField").assertExists()
        composeTestRule.onNodeWithTag("PasswordField").assertExists()

        // Check for Logo Text
        composeTestRule.onNodeWithTag("LogoText").assertExists()

        // Check for Buttons
        composeTestRule.onNodeWithTag("LoginButton").assertExists()
        composeTestRule.onNodeWithTag("ForgotPasswordButton").assertExists()
        composeTestRule.onNodeWithTag("CreateAccountButton").assertExists()
    }
}
