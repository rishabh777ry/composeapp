package com.example.composeapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composeapp.data.entity.User

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user:User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int) : User?
    @Query("DELETE FROM users")
     suspend fun deleteAllUsers()
}