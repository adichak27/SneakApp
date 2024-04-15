package com.cs4520.sneak.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Shoe::class, User::class], version = 4,exportSchema = false)
abstract class SneakDB: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun shoeDao(): ShoeDao

    companion object{
        private var instance: SneakDB? = null

        @Synchronized
        fun getInstance(context: Context): SneakDB {
            if(instance == null)
                instance = Room.databaseBuilder(context.applicationContext,
                    SneakDB::class.java, "sneak_database",)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }
        private val roomCallback = object :Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
}