package com.cs4520.sneak.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs4520.sneak.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {

    private lateinit var repo: UserRepository
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    // Initialize the repository
    fun initialize(repository: UserRepository) {
        this.repo = repository
    }

    // Retrieves the product list from the repository in a coroutine
    fun fetchUsers() {
        viewModelScope.launch {
            try {
                // Try fetching the products from the repo
                val userList = repo.getAllUsers()
                // Make sure the product list is not null or empty
                if (!userList.isNullOrEmpty()) {
                    //_users.value = userList
                }
            } catch (e: Exception) {
                // Catch any error exception and set it to the error value
                _error.value = e.message
            }
        }
    }
}