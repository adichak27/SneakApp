package com.cs4520.sneak.model

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.cs4520.sneak.data.ShoeRepoTest
import com.cs4520.sneak.data.ApiService
import com.cs4520.sneak.data.DatabaseClient
import com.cs4520.sneak.data.ShoeRepository
import com.cs4520.sneak.data.SneakApi
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.model.ProductViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


class TestApplication : Application() {
    override fun getApplicationContext(): Context {
        return this
    }
}

class ProductListViewModelTest {

    private lateinit var shoeListViewModel: ProductViewModel

    private lateinit var mockShoes: List<Shoe>

    @MockK
    private lateinit var mockApplication: TestApplication


    private var mockContext = mockk<Context>(relaxed = true)

    @MockK
    private lateinit var mockShoeRepository: ShoeRepository




    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()






    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.IO)

        MockKAnnotations.init(this)


        every {
            mockApplication.applicationContext
        } returns mockContext

        every { ShoeRepository(mockApplication) } returns mockShoeRepository

        shoeListViewModel = ProductViewModel(mockApplication)

        mockShoes = listOf(
            Shoe("Samba", "Adidas", "lifestyle", 100.00, 0),
            Shoe("Air Force", "Nike", "lifestyle", 90.00, 1),
            Shoe("Chuck Taylor All Star", "Converse", "lifestyle", 55.00, 2)
        )
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testFetchShoesOnInit() = runTest {
        coEvery {mockShoeRepository.getAllShoes()} returns mockShoes


        val actual = shoeListViewModel.uiState.value


//        verify { runBlocking { mockShoeRepository.getAllShoes() } }
        val expected = ShoeUiState.Success(mockShoes)


        assertEquals(expected, actual)

    }

    @Test
    fun toggleCartItemSuccessful() {
        // make sure cart list is empty
        assertEquals(0, shoeListViewModel.cartItems.value?.size ?: 0)

        shoeListViewModel.toggleCartItem(Shoe("Samba", "Adidias", "lifestyle", 100.00, 0))

        // now cart list should have one shoe
        assertEquals(1, shoeListViewModel.cartItems.value?.size ?: 1)

    }

    @Test
    fun toggleCartItemOffSuccessful() {
        // make sure cart list is empty
        assertEquals(0, shoeListViewModel.cartItems.value?.size ?: 0)

        shoeListViewModel.toggleCartItem(Shoe("Samba", "Adidias", "lifestyle", 100.00, 0))

        // now cart list should have one shoe
        assertEquals(1, shoeListViewModel.cartItems.value?.size ?: 1)

        shoeListViewModel.toggleCartItem(Shoe("Samba", "Adidias", "lifestyle", 100.00, 0))

        // now cart list should be back to zero shoes
        assertEquals(0, shoeListViewModel.cartItems.value?.size ?: 1)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateUIStateChangesUIState() = runTest {
        // state is currently loading
        assertEquals(ShoeUiState.Loading,shoeListViewModel.uiState.value)

        shoeListViewModel.updateUiState(ShoeUiState.Success(mockShoes))

        // ShoeUiState is updated
        assertEquals(ShoeUiState.Success(mockShoes),shoeListViewModel.uiState.value)
    }
}



