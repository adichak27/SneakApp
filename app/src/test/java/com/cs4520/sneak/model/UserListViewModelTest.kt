package com.cs4520.sneak.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cs4520.sneak.data.UserRepository
import com.cs4520.sneak.data.database.User
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

class UserListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userRepository: UserRepository
    private lateinit var userListViewModel: UserListViewModel



    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        userRepository = mock(UserRepository::class.java)
        userListViewModel = UserListViewModel()
        userListViewModel.initialize(userRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun testFetchUsersSuccess() = runTest {
        // Given
        val users = listOf(User("user1", "email1", "password1"), User("user2", "email2", "password2"))
        val userListFlow = MutableStateFlow(users)
        `when`(userRepository.getAllUsers()).thenReturn(userListFlow.value)

        // When
        userListViewModel.fetchUsers()

        // Then
        assertEquals(users, userListViewModel.users.value)
    }

}