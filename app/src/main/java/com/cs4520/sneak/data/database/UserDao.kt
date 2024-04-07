package com.cs4520.sneak.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: Users)

    @Query("select * from user_table")
    fun getAllUsers(): List<Users>

    @Query("select * from user_table u where u.username = :username")
    fun getUser(username: String): Users

    @Query("Update user_table set password = :newPass, username = :newName where username = :username")
    fun editUser(username: String, newName: String, newPass: String)


}