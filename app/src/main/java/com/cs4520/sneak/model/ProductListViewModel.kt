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


sealed class ShoeUiState {
    object Loading : ShoeUiState()
    data class Success(val shoes: List<Shoe>) : ShoeUiState()
    data class Error(val exception: Throwable) : ShoeUiState()
}

class ProductViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = ShoeRepository(application)

    private val _cartItems = mutableStateOf<List<String>>(emptyList())

    val cartItems: State<List<String>> = _cartItems

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
        val currentCartItems = _cartItems.value.toMutableList()
        if (currentCartItems.contains(shoe.name)) {
            currentCartItems.remove(shoe.name)
            Log.d("RemoveItem", "Shoe is " + shoe.name)
            Log.d("RemoveItemResult", "Result is " + _cartItems.value.toMutableList().contains(shoe.name))

        } else {
            Log.d("AddItem", "Shoe is " + shoe.name)
            currentCartItems.add(shoe.name)
        }
        _cartItems.value = currentCartItems
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