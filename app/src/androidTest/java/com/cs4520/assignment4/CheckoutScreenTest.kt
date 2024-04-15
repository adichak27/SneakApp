package com.cs4520.assignment4

import androidx.compose.ui.test.assertTextEquals
import com.cs4520.sneak.CheckoutScreen
import com.cs4520.sneak.MainActivity
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cs4520.sneak.model.ProductViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.cs4520.sneak.data.database.Shoe

@RunWith(AndroidJUnit4::class)
class CheckoutScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            CheckoutScreen(navController, viewModel = ProductViewModel(ApplicationProvider.getApplicationContext()))
        }
    }

    @Test
    fun checkoutScreenComponentsAreDisplayed() {
        // Check for Input Fields
        composeTestRule.onNodeWithTag("CardNumberInput").assertExists()
        composeTestRule.onNodeWithTag("ExpirationDateInput").assertExists()
        composeTestRule.onNodeWithTag("CVVInput").assertExists()

        // Check for subtotal, tax, and total displays
        composeTestRule.onNodeWithTag("SubtotalText").assertExists()
        composeTestRule.onNodeWithTag("TaxText").assertExists()
        composeTestRule.onNodeWithTag("TotalText").assertExists()

        // Check for Purchase Button
        composeTestRule.onNodeWithTag("PurchaseButton").assertExists()
    }
    @Test
    fun subtotalTaxTotalDisplaysCorrectly() {

        val viewModel = mock(ProductViewModel::class.java)
        val cartItemsLiveData = MutableLiveData(listOf(
            Shoe("Shoe1", "Nike", "Running", 100.0, 4),
            Shoe("Shoe2", "Reebok", "Running", 50.0, 2)
        ))
        `when`(viewModel.cartItems).thenReturn(cartItemsLiveData)

        // Setup the test content
        composeTestRule.setContent {
            CheckoutScreen(rememberNavController(), viewModel)
        }

        // Define expected values based on the test ViewModel's setup
        val expectedSubtotal = 150.0 // Sum of shoe prices
        val expectedTax = expectedSubtotal * 0.06
        val expectedTotal = expectedSubtotal + expectedTax

        // Test assertions
        composeTestRule.onNodeWithTag("SubtotalText").assertTextEquals("Subtotal: $${"%.2f".format(expectedSubtotal)}")
        composeTestRule.onNodeWithTag("TaxText").assertTextEquals("Tax (6%): $${"%.2f".format(expectedTax)}")
        composeTestRule.onNodeWithTag("TotalText").assertTextEquals("Total: $${"%.2f".format(expectedTotal)}")
    }
/*
    @Test
    fun ensureToastAppearsForInvalidInputs() {
        // Simulate incorrect credit card number input
        composeTestRule.onNodeWithTag("CardNumberInput").performTextInput("123456789")
        composeTestRule.onNodeWithTag("ExpirationDateInput").performTextInput("13/50") // Invalid date
        composeTestRule.onNodeWithTag("CVVInput").performTextInput("12") // Invalid CVV

        // Attempt to proceed with purchase
        composeTestRule.onNodeWithText("Purchase").performClick()

        // Using Espresso to check for the toast message
        Thread.sleep(2000)
        //onView(withText("Invalid card number: must be 10 digits")).check(matches(isDisplayed()))
        // onView(withText("Invalid month in expiration date: must be between 01 and 12")).check(matches(isDisplayed()))
        // onView(withText("Invalid CVV: must be 3 digits")).check(matches(isDisplayed()))
    }

 */
}
