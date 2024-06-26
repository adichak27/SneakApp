package com.cs4520.sneak.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("select * from user_table")
    fun getAllUsers(): LiveData<List<User>>

    @Query("select * from user_table u where u.username = :username and u.password = :password")
    fun getUser(username: String, password:String): User?

    @Query("Update user_table set password = :newPass, username = :newName where username = :username")
    fun editUser(username: String, newName: String, newPass: String)


}