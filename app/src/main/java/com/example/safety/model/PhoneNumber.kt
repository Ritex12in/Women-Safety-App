package com.example.safety.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_phoneNumber")
data class PhoneNumber(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String
)
