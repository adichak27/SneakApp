package com.cs4520.assignment4.data

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.cs4520.sneak.data.UserRepository
import com.cs4520.sneak.data.database.User
import com.cs4520.sneak.data.database.UserDao
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserRepoTest {

    private val fakeUserDao = FakeUserDao()
    private val userRepo = UserRepository(fakeUserDao)



    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Before
    fun mockLogClass() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

 //
// {"email": "test@gmail.com", "username": 'admin', "password": 'admin'},
//    {"email": "fake@gmail.com", "username": 'bob', "password": '123'},
//    {"email": "test@aol.com", "username": "brandon", "password": "password"},
//    {"email": "pat@yahoo.com", "username": "pat", "password": "sneak"}

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllUsersFromApiSuccessful() = runTest {

        val expected = listOf(
            User("admin", "test@gmail.com", "admin"),
            User("bob", "fake@gmail.com", "123"),
            User("brandon", "test@aol.com", "password"),
            User("pat", "pat@yahoo.com", "sneak")
        )

        val actual = userRepo.getAllUsers()

        assertEquals(expected, actual)

    }

    @Test
    fun getUserNameBrandonSuccess() = runTest {
        val expected = User("brandon", "test@aol.com", "password")

        val actual = userRepo.getUser("brandon")

        assertEquals(expected, actual)
    }

    @Test
    fun getUserNamePhilNotSuccess() = runTest {

        val actual = userRepo.getUser("phil")

        assertEquals(null,actual)
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun editUserBrandonSuccess() = runTest {
//
//        val oldExpected = User("brandon", "test@aol.com", "password")
//
//        val oldActual = userRepo.getUser("brandon")
//
//        // set initial value of user brandon
//        assertEquals(oldExpected, oldActual)
//
//
//        // editing user brandon
//        val body: Map<String, String> = mapOf("password" to "new_password")
//        userRepo.editUser("brandon", body)
//
//        val newExpected = User("brandon", "test@aol.com", "new_password")
//
//        // getting edited user brandon
//        val newActual = userRepo.getUser("brandon")
//
//        assertEquals(newExpected,newActual)
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addNewUserSuccessful() = runTest {
        //TODO: add test

    }

    class FakeUserDao: UserDao {
        override suspend fun insert(user: User) {
            TODO("Not yet implemented")
        }

        override fun getAllUsers(): LiveData<List<User>> {
            TODO("Not yet implemented")
        }

        override fun getUser(username: String, password: String): User? {
            TODO("Not yet implemented")
        }

        override fun editUser(username: String, newName: String, newPass: String) {
            TODO("Not yet implemented")
        }

    }
}