package com.example.safety.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_email")
data class Email(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "email") val email: String
)
