package com.cs4520.sneak.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cs4520.sneak.data.ApiService
import com.cs4520.sneak.data.ShoeRepository
import com.cs4520.sneak.data.UserRepository
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.data.database.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Response


class UserListViewModelTest {
    private val api = mockk<ApiService>()

    private var userRepo = UserRepository(api)

    private lateinit var userList: List<User>

    @MockK
    private lateinit var mockUserRepository: UserRepository

    private lateinit var userListViewModel: UserListViewModel

    @MockK
    private lateinit var mockApplication: TestApplication

    private var mockContext = mockk<Context>(relaxed = true)


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        MockKAnnotations.init(this)

        every {
            mockApplication.applicationContext
        } returns mockContext

        userListViewModel = UserListViewModel(mockApplication)


        userListViewModel.initialize(mockUserRepository)

    }

    @Before
    fun makeMockApi() {
        userList = listOf(
            User("admin", "test@gmail.com", "admin"),
            User("bob", "fake@gmail.com", "123"),
            User("brandon", "test@aol.com", "password"),
            User("pat", "pat@yahoo.com", "sneak")
        )

        coEvery { api.getUsers()  } returns Response.success(userList)
        coEvery { api.getUser("brandon") } returns Response.success(userList.filter { it.username == "brandon" }[0])
        coEvery { api.getUser("phil") } returns Response.success(null)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test fetchUsers success`() = runTest {
        val userListFlow = MutableStateFlow(userList)
        coEvery { mockUserRepository.getAllUsers() } returns userListFlow.value

        // When
        userListViewModel.fetchUsers()

        // Then
        assertEquals(userList, userListViewModel.users.first())
    }


}