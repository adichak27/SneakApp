package com.cs4520.sneak.data

import android.util.Log
import com.cs4520.sneak.data.database.User
import com.cs4520.sneak.data.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun getUser(userName: String): User? {
        return try {
            val response = apiService.getUser(userName)
            if (response.isSuccessful) {
                response.body()
            } else {
                throw Exception("Error Occurred: ${response.body()}")
            }
        } catch (ex: Exception) {
            Log.e("UserRepo", "Error getting users", ex)
            throw Exception("Error Occurred: ${ex.message}")

        }
    }

    suspend fun editUser(userName: String, body: Map<String, String>) {
        // body would just be a mapOf("password" to "<newPass>) I think
        try {
            val response = apiService.editUser(userName, body)
            if (response.isSuccessful) {
                Log.w("UserRepo", "Edited User $userName")
            } else {
                throw Exception("Error Occurred: ${response.body()}")
            }
        } catch (ex: Exception) {
            Log.e("UserRepo", "Error editing user $userName", ex)
            throw Exception("Error Occurred: ${ex.message}")

        }
    }

    suspend fun loadUsersToDb(users: List<User>) {
        withContext(Dispatchers.IO) {
            for (user in users) {
                userDao.insert(user)
            }
        }
    }

    suspend fun getUserFromDb(userName: String, password: String): User {
        return withContext(Dispatchers.IO) {
            val user = userDao.getUser(userName, password)
            user ?: throw NoSuchElementException("No user with $userName and $password")
        }
    }

    suspend fun editUser(userName: String, newName: String?, newPassword: String) {
        withContext(Dispatchers.IO) {
            if (newName != null) {
                userDao.editUser(userName, newName, newPassword)
            } else {
                userDao.editUser(userName, userName, newPassword)
            }
        }

    }

    suspend fun registerNewUser(userName: String, email: String, password: String) {
        withContext(Dispatchers.IO) {
            val newUser = User(userName, email, password)
            userDao.insert(newUser)
        }
    }

}