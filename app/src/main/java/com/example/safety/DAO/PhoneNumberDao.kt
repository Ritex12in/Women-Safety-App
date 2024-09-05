package com.example.safety.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.safety.model.PhoneNumber
import kotlinx.coroutines.flow.Flow

@Dao
interface PhoneNumberDao {
    @Query("SELECT * FROM table_phoneNumber")
    fun getAll():Flow<List<PhoneNumber>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(phoneNumber: PhoneNumber)

    @Query("DELETE FROM table_phoneNumber WHERE id = :id")
    suspend fun deleteById(id:Int)
}