package com.cs4520.sneak.data

import android.util.Log
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.data.database.ShoeDao
import com.cs4520.sneak.data.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShoeRepository(val shoeDao: ShoeDao) {

    private val apiService = SneakApi.apiService

    suspend fun getAllShoes(): List<Shoe> {

        return try {
            val response = apiService.getShoes()
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

    suspend fun loadUsersToDb(shoes: List<Shoe>) {
        withContext(Dispatchers.IO) {
            for (shoe in shoes) {
                shoeDao.insertShoe(shoe)
            }
        }
    }

    suspend fun getShoesFromDB(): List<Shoe> {
        var res: List<Shoe> = emptyList()
        withContext(Dispatchers.IO) {
            res = shoeDao.getAllShoes()
        }
        return res
    }
    }