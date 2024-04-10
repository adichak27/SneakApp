package com.cs4520.sneak.data

import android.util.Log
import com.cs4520.sneak.data.database.User
import com.cs4520.sneak.data.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Potentially make interface for repo
class UserRepository(val userDao: UserDao) {

    private val apiService = SneakApi.apiService

    suspend fun getAllUsers(): List<User> {

        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {

                response.body() ?: emptyList()

            } else {
                throw Exception("Error Occurred: ${response.body()}")
            }

        } catch (ex: Exception) {
            Log.e("ProductRepository", "Error getting products", ex)
            emptyList()

        }
    }

    suspend fun loadUsersToDb(users: List<User>) {
            for (user in users) {
                userDao.insert(user)
            }
    }

    fun getUserFromDb(userName: String, password: String) : User {
        val user = userDao.getUser(userName, password)
        return user ?: throw NoSuchElementException("No user with $userName and $password")
    }

    suspend fun editUser(userName: String, newName:String?, newPassword:String){
        withContext(Dispatchers.IO) {
            if (newName != null) {
                userDao.editUser(userName, newName, newPassword)
            } else {
                userDao.editUser(userName, userName, newPassword)
            }
        }

    }

    suspend fun registerNewUser(userName: String, email:String, password:String) {
        withContext(Dispatchers.IO) {
            val newUser = User(userName, email, password)
            userDao.insert(newUser)
        }
    }

}