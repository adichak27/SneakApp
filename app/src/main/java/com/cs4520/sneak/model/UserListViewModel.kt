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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

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
                    repo.loadUsersToDb(userList)
                }
            } catch (e: Exception) {
                // Catch any error exception and set it to the error value
                _error.value = e.message
            }
        }
    }

    // Adds a new user to the user repository
    fun addUser(userName: String, email : String, password : String) {
        viewModelScope.launch(Dispatchers.IO) {
            //repo.addNewUser(userName, email, password)
        }
    }

    fun setCurrentUser(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _currentUser.value = repo.getUser(userName)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}