package com.example.safety.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.safety.model.Email
import kotlinx.coroutines.flow.Flow

@Dao
interface EmailDao {
    @Query("SELECT * FROM table_email")
    fun getAll():Flow<List<Email>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(email: Email)

    @Query("DELETE FROM table_email WHERE id = :id")
    suspend fun deleteById(id:Int)
}