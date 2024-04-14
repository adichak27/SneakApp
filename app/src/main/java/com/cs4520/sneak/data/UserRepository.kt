package com.cs4520.sneak.data

import android.util.Log
import com.cs4520.sneak.data.database.User
import com.cs4520.sneak.data.database.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Potentially make interface for repo
class UserRepository(val apiService: ApiService=SneakApi.apiService ) {

    suspend fun getAllUsers(): List<User> {

        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {

                response.body() ?: emptyList()

            } else {
                throw Exception("Error Occurred: ${response.body()}")
            }

        } catch (ex: Exception) {
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
            throw Exception("Error Occurred: ${ex.message}")

        }
    }

    suspend fun editUser(userName: String, body: Map<String, String>): Any? {
        // body would just be a mapOf("password" to "<newPass>) I think
        try {
            val response = apiService.editUser(userName, body)
            if (response.isSuccessful) {
                return response.body()
            } else {
                throw Exception("Error Occurred: ${response.body()}")
            }
        } catch (ex: Exception) {
            Log.e("UserRepo", "Error editing user $userName", ex)
            throw Exception("Error Occurred: ${ex.message}")

        }
    }

    suspend fun addNewUser(body: Map<String, String>): Any? {
        // body would just be a
        // mapOf("email" to "<email", "username" to "<username>", "password" to "<newPass>) I think
        try {
            val response = apiService.addNewUser(body)
            if (response.isSuccessful) {
                return response.body()
            } else {
                throw Exception("Error Occurred: ${response.body()}")
            }
        } catch (ex: Exception) {

            throw Exception("Error Occurred: ${ex.message}")

        }
    }

}