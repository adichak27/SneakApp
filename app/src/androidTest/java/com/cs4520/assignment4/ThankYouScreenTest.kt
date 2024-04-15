package com.cs4520.assignment4

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cs4520.sneak.CheckoutScreen
import com.cs4520.sneak.MainActivity
import com.cs4520.sneak.ThanksScreen
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.model.ProductViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ThankYouScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkoutScreenComponentsAreDisplayed() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            ThanksScreen(
                navController,
                viewModel = ProductViewModel(ApplicationProvider.getApplicationContext())
            )
        }
        // Check for Input Fields
        composeTestRule.onNodeWithTag("ThanksForPurchase").assertExists()
        composeTestRule.onNodeWithTag("ShippingText").assertExists()
    }

    @Test
    fun estimatedShippingCorrectlyDisplayed() {
        val shoeListViewModel = ProductViewModel(ApplicationProvider.getApplicationContext())
        val shoe1 = Shoe("Shoe1", "Nike", "Running", 100.0, 4)
        val shoe2 = Shoe("Shoe2", "Reebok", "Running", 50.0, 2)

        shoeListViewModel.toggleCartItem(shoe1)
        shoeListViewModel.toggleCartItem(shoe2)

        composeTestRule.setContent {
            val navController = rememberNavController()
            ThanksScreen(
                navController,
                shoeListViewModel
            )
        }


        composeTestRule.onNodeWithTag("ShippingText").assertTextEquals("Your products will arrive in approximately 2 days.")
    }
}