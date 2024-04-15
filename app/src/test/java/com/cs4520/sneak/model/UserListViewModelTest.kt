package com.cs4520.sneak.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs4520.sneak.data.ApiService
import com.cs4520.sneak.data.UserRepository
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
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response


class UserListViewModelTest {
    private val api = mockk<ApiService>()

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
    fun fetchUsersSuccess() = runTest {
        val userListFlow = MutableStateFlow(userList)
        coEvery { mockUserRepository.getAllUsers() } returns userListFlow.value

        // When
        userListViewModel.fetchUsers()

        // Then
        assertEquals(userList, userListViewModel.users.first())
    }

    // TODO: FIX THIS TEST
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addUserSuccess() = runTest {
        // Given
        val userName = "testUser"
        val email = "test@example.com"
        val password = "password"
        val actualUser = User(userName, email, password)
        val body = mapOf(
            "email" to email,
            "username" to userName,
            "password" to password
        )
        coEvery { mockUserRepository.addNewUser(body) } returns Unit // Mocking the repository function to return Unit

        // When
        userListViewModel.addUser(userName, email, password)

        // Then
        coEvery { mockUserRepository.addNewUser(body) } // Ensure the function is called with the correct parameters

        assertEquals(listOf(actualUser), userListViewModel.users.first())
    }

    @Test
    fun setCurrentUserSuccess() {

    }

    @Test
    fun editUserSuccess() {

    }

    @Test
    fun setLoginErrorMessageSuccess() {
        userListViewModel.setLoginErrorMessage("Error message")
        assertEquals("Error message", userListViewModel.loginError.value)

    }
    @Test
    fun setPasswordErrorMessageSuccess() {
        userListViewModel.setPasswordErrorMessage("Error message")
        assertEquals("Error message", userListViewModel.resetPasswordError.value)
    }


    @Test
    fun clearLoginErrorSuccess() {
        // Given
        userListViewModel.setLoginErrorMessage("Error message")

        // When
        userListViewModel.clearLoginError()

        // Then
        assertEquals(null, userListViewModel.loginError.value)
    }

    @Test
    fun clearResetPasswordErrorSuccess() {
        // Given
        userListViewModel.setPasswordErrorMessage("Error message")

        // When
        userListViewModel.clearResetPasswordError()

        // Then
        assertEquals(null, userListViewModel.resetPasswordError.value)
    }

}