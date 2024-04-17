package com.cs4520.assignment4

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cs4520.sneak.MainActivity
import com.cs4520.sneak.ProductListScreen
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.model.ProductViewModel
import com.cs4520.sneak.model.ShoeUiState
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Test
    fun productListDisplaysCorrectly() {
        val viewModel = ProductViewModel(ApplicationProvider.getApplicationContext())
        // Preparing some mock data
        val shoe1 = Shoe("Shoe1", "Converse", "Running", 100.0, 4)
        val shoe2 = Shoe("Shoe2", "Nike", "Running", 50.0, 2)

        val shoesLiveData: LiveData<List<Shoe>> = MutableLiveData(listOf(shoe1, shoe2))

        viewModel.cartItems = shoesLiveData

        composeTestRule.setContent {
            ProductListScreen(rememberNavController(), viewModel)
        }

        // Assertions
        composeTestRule.onNodeWithText("Products").assertIsDisplayed()
        composeTestRule.onNodeWithTag("CheckoutTag").assertTextEquals("Checkout(2) items")
        composeTestRule.onNodeWithTag("Nike").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Converse").assertIsDisplayed()
    }

    @Test
    fun updateCartOnAddItem() {
        val viewModel = ProductViewModel(ApplicationProvider.getApplicationContext())
        // Preparing some mock data
        val shoe1 = Shoe("Shoe1", "Converse", "Running", 100.0, 4)

        val shoesLiveData: LiveData<List<Shoe>> = MutableLiveData(listOf(shoe1))
        viewModel.cartItems = shoesLiveData

        composeTestRule.setContent {
            ProductListScreen(rememberNavController(), viewModel)
        }

        // Assertions
        composeTestRule.onNodeWithText("Products").assertIsDisplayed()
        Assert.assertEquals(1, viewModel.cartItems.value?.size ?: 0)
        composeTestRule.onNodeWithTag("CheckoutTag").assertTextEquals("Checkout(1) items")

        //composeTestRule.onNodeWithTag("Nike").performClick()
        viewModel.toggleCartItem(Shoe("Samba", "Vans", "lifestyle", 100.00, 0))
        Assert.assertEquals(1, viewModel.cartItems.value?.size ?: 0)

        composeTestRule.onNodeWithTag("Vans").assertTextEquals("Add")
        composeTestRule.onNodeWithTag("CheckoutTag").assertTextEquals("Checkout(1) items")
    }
}

