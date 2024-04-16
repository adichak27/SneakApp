package com.cs4520.assignment4

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
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
}