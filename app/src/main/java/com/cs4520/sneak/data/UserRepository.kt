package com.cs4520.sneak.data

import android.util.Log
import com.cs4520.sneak.data.database.UserDao
import com.cs4520.sneak.data.database.Users
import com.cs4520.sneak.data.database.toUser
import com.cs4520.sneak.model.User
import com.cs4520.sneak.model.toUsers

class UserRepository(val userDao: UserDao) {

    private val apiService = SneakApi.apiService

    suspend fun getAllUsers(): List<Users> {

        return try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {

                convertToUsersList(response.body() ?: emptyList())

            } else {
                throw Exception("Error Occurred: ${response.body()}")
            }

        } catch (ex: Exception) {
            Log.e("ProductRepository", "Error getting products", ex)
            emptyList()

        }
    }

    fun loadUsersToDb(users: List<Users>) {
       for(user in users){
           userDao.insert(user)
       }
    }

    fun getUserFromDb(userName: String, password: String): User {
        val userFromDb = userDao.getUser(userName, password)
            ?: throw NoSuchElementException("No user with $userName and $password")
        return userFromDb.toUser()
    }

    fun editUser(userName: String, newName:String?, newPassword:String){
        if(newName != null) {
            userDao.editUser(userName, newName, newPassword)
        }
        else {
            userDao.editUser(userName, userName, newPassword)
        }

    }

    fun registerNewUser(userName: String, email:String, password:String) {
        val newUser = Users(userName, email, password)
        userDao.insert(newUser)
    }

    private fun convertToUsersList(users: List<User>): List<Users> {
        return users.map { it.toUsers() }

    }
}