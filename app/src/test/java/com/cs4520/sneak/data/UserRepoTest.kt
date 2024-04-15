package com.cs4520.sneak.data

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.cs4520.sneak.data.ApiService
import com.cs4520.sneak.data.UserRepository
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.data.database.User
import com.cs4520.sneak.data.database.UserDao
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class UserRepoTest {

    private val api = mockk<ApiService>()
    private var userRepo = UserRepository(api)
    private lateinit var userList: List<User>



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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getUserNamePhilNotSuccess() = runTest {

        val actual = userRepo.getUser("phil")

        assertEquals(null,actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun editUserBrandonSuccess() = runTest {

        val oldExpected = User("brandon", "test@aol.com", "password")

        val oldActual = userRepo.getUser("brandon")

        // set initial value of user brandon
        assertEquals(oldExpected, oldActual)

        val newExpected = User("brandon", "test@aol.com", "new_password")

        coEvery { api.editUser("brandon", mapOf("password" to "new_password")) } returns Response.success("Changed Brandon password to new_password" )
        coEvery { api.getUser("brandon") } returns Response.success(newExpected )

        // editing user brandon
        val body: Map<String, String> = mapOf("password" to "new_password")
        userRepo.editUser("brandon", body)



        // getting edited user brandon
        val newActual = userRepo.getUser("brandon")

        assertEquals(newExpected,newActual)
        verify { runBlocking{api.editUser("brandon", mapOf("password" to "new_password")) } }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addNewUserSuccessful() = runTest {
        val newUserBody = mapOf("email" to "test@gmail.com",
            "username" to "TEST",
            "password" to "TEST")
        coEvery { api.addNewUser(newUserBody) } returns Response.success("New User Created ${newUserBody["username"]}")

        val expected = "New User Created ${newUserBody["username"]}"

        val actual = userRepo.addNewUser(newUserBody)

        assertEquals(expected,actual)


    }

}