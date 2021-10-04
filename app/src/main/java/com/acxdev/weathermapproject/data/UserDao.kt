package com.acxdev.weathermapproject.data

import androidx.room.*
import com.acxdev.weathermapproject.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE username = :username")
    suspend fun getUser(username: String): User

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT EXISTS (SELECT 1 FROM user WHERE username = :username)")
    suspend fun isUsernameExist(username: String): Boolean
}