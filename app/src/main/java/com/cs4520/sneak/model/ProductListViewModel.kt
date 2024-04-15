package com.cs4520.sneak.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.cs4520.sneak.data.ShoeRepository
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.data.database.ShoeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.asStateFlow


sealed class ShoeUiState {
    object Loading : ShoeUiState()
    data class Success(val shoes: List<Shoe>) : ShoeUiState()
    data class Error(val exception: Throwable) : ShoeUiState()
}

class ProductViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = ShoeRepository(application)

    private val _cartItems = MutableLiveData<List<Shoe>>(emptyList())
    val cartItems: LiveData<List<Shoe>> = _cartItems

    private val _uiState = MutableStateFlow<ShoeUiState>(ShoeUiState.Loading)
    val uiState: StateFlow<ShoeUiState> = _uiState

    init {
        fetchShoes()
        scheduleFetchProductsWork()
    }

    private fun scheduleFetchProductsWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<FetchProductsWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(getApplication<Application>().applicationContext).enqueueUniquePeriodicWork(
            "FetchProductsWork",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
    }
    fun toggleCartItem(shoe: Shoe) {
        val currentCartItems = _cartItems.value?.toMutableList() ?: mutableListOf()
        if (currentCartItems.any { it.name == shoe.name }) {
            currentCartItems.removeAll { it.name == shoe.name }
        } else {
            currentCartItems.add(shoe)
        }
        _cartItems.postValue(currentCartItems)
    }

    private fun fetchShoes(page: Int? = null) {
        viewModelScope.launch {
            _uiState.value = ShoeUiState.Loading
            try {
                val products = repository.getAllShoes()
                _uiState.value = ShoeUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ShoeUiState.Error(e)
            }
        }
    }
    fun fetchLatestShoesImmediately() {
        val workRequest = OneTimeWorkRequestBuilder<FetchProductsWorker>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(getApplication<Application>().applicationContext).enqueueUniqueWork(
            "FetchLatestProductsWork",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}

class FetchProductsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val appContext = applicationContext
            val repository = ShoeRepository(appContext)

            // Insert shoes into the database, checking for duplicates as needed
            repository.getAllShoes()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}