package com.cs4520.sneak.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.cs4520.sneak.data.database.Shoe
import com.cs4520.sneak.data.database.ShoeDao
import com.cs4520.sneak.data.database.SneakDB
import com.cs4520.sneak.data.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseClient {
    private var instance: SneakDB? = null

    fun getDatabase(context: Context): SneakDB {
        if (instance == null) {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SneakDB::class.java, "database-name"
                    ).fallbackToDestructiveMigration().build()
                }
            }
        }
        return instance!!
    }
}


class ShoeRepository(context: Context) {
    private val apiService = SneakApi.apiService
    private val shoeDao = DatabaseClient.getDatabase(context).shoeDao()

    suspend fun getAllShoes(): List<Shoe> {
        try {
            val response = apiService.getShoes()
            if (response.isSuccessful) {
                val shoes = response.body()!!
                shoeDao.insertAll(shoes)
                return shoes
            } else {
                return shoeDao.getAllShoes()
            }
        } catch (ex: Exception) {

            return shoeDao.getAllShoes()
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