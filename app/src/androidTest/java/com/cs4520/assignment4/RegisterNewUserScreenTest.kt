package com.cs4520.assignment4

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cs4520.sneak.LoginScreen
import com.cs4520.sneak.MainActivity
import com.cs4520.sneak.RegisterNewUserScreen
import com.cs4520.sneak.model.UserListViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterNewUserScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

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
}