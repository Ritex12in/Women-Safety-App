package com.example.safety.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.safety.DAO.EmailDao
import com.example.safety.DAO.PhoneNumberDao
import com.example.safety.model.Email
import com.example.safety.model.PhoneNumber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [PhoneNumber::class, Email::class], version = 1, exportSchema = false)
public abstract class ContactRoomDatabase:RoomDatabase() {
    abstract fun phoneNumberDao():PhoneNumberDao
    abstract fun emailDao():EmailDao

    companion object{
        @Volatile
        private var INSTANCE:ContactRoomDatabase? = null

        fun getDatabase(context: Context, scope:CoroutineScope): ContactRoomDatabase {
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactRoomDatabase::class.java,
                    "contact_database"
                )
                    .addCallback(ContactDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class ContactDatabaseCallback(private val scope: CoroutineScope): Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let{database->
                scope.launch {
                    //populateDatabase(database.emailDao())
                }
            }
        }

//        suspend fun populateDatabase(emailDao: EmailDao){
//            var email = Email(email = "xyz@gmail.com")
//            emailDao.insert(email)
//            email = Email(email = "abcd@gmail.com")
//            emailDao.insert(email)
//        }
    }
}