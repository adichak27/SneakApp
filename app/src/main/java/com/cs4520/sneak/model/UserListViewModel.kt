package com.cs4520.sneak.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs4520.sneak.data.UserRepository
import com.cs4520.sneak.data.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserListViewModel() : ViewModel() {

    private lateinit var repo: UserRepository

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> get() = _loginError

    private val _resetPasswordError = MutableStateFlow<String?>(null)
    val resetPasswordError: StateFlow<String?> get() = _resetPasswordError

    // Initialize the repository
    fun initialize(repository: UserRepository) {
        this.repo = repository
    }

    // Retrieves the user list from the repository in a coroutine
    // TODO: Potentially move to Repository
    fun fetchUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Try fetching the products from the repo
                val userList = repo.getAllUsers()
                // Make sure the product list is not null or empty
                if (!userList.isNullOrEmpty()) {
                    _users.value = userList
                }
            } catch (e: Exception) {
                // Catch any error exception and set it to the error value
                _loginError.value = e.message
            }
        }
    }

    // Adds a new user to the user repository
    fun addUser(userName: String, email : String, password : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val body = mapOf(
                "email" to email,
                "username" to userName,
                "password" to password
            )
            repo.addNewUser(body)
        }
    }

    fun setCurrentUser(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _currentUser.value = repo.getUser(userName)
            } catch (e: Exception) {
                _loginError.value = e.message
            }
        }
    }

    fun clearLoginError() {
        _loginError.value = null
    }

    fun editUser(userName: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val body = mapOf("password" to newPassword)
            try {
                repo.editUser(userName, body)
                println("Editing user: $userName")
            } catch (e: Exception) {
                _resetPasswordError.value = e.message
                println("Editing user error: " +  _resetPasswordError.value)
            }
        }
    }

    fun clearResetPasswordError() {
        _resetPasswordError.value = null
    }
}