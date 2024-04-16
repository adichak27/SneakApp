package com.cs4520.assignment4

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.cs4520.sneak.AmazingProductsApp
import com.cs4520.sneak.MainActivity
import com.cs4520.sneak.PasswordReset
import com.cs4520.sneak.RegisterNewUserScreen
import com.cs4520.sneak.model.UserListViewModel
import org.junit.Rule
import org.junit.Test

class ResetPasswordScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun resetPasswordScreenComponentsAreDisplayed() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            PasswordReset(navController, vm = UserListViewModel(ApplicationProvider.getApplicationContext()))
        }
        // Check for Input Fields
        composeTestRule.onNodeWithTag("UserNameInput").assertExists()
        composeTestRule.onNodeWithTag("NewPasswordInput").assertExists()
        composeTestRule.onNodeWithTag("ConformNewPasswordInput").assertExists()


        // Check for Text
        composeTestRule.onNodeWithTag("LogoText").assertExists()
        composeTestRule.onNodeWithTag("ChangePasswordText").assertExists()


        // Check for Buttons
        composeTestRule.onNodeWithTag("ConfirmButton").assertExists()
    }

    @Test
    fun resetPasswordNavigatesToLoginScreen() {
        composeTestRule.setContent {
            AmazingProductsApp()
        }


        // moving to ForgotPassword screen
        composeTestRule.onNodeWithTag("ForgotPasswordButton").performClick()

        // on forgot password screen, change the password of the user brandon
        val userName = "brandon"
        composeTestRule.onNodeWithTag("UserNameInput").performTextInput(userName)

        val newPassword = "newPassword"
        composeTestRule.onNodeWithTag("NewPasswordInput").performTextInput(newPassword)
        val confirmPassword = "newPassword"
        composeTestRule.onNodeWithTag("ConformNewPasswordInput").performTextInput(confirmPassword)

        composeTestRule.onNodeWithTag("ConfirmButton").performClick()

        // check we are on the login screen
        composeTestRule.onNodeWithTag("LoginButton").assertExists()
        composeTestRule.onNodeWithTag("ForgotPasswordButton").assertExists()
        composeTestRule.onNodeWithTag("CreateAccountButton").assertExists()

    }
}