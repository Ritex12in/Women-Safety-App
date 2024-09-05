package com.example.safety.repository

import androidx.annotation.WorkerThread
import com.example.safety.DAO.EmailDao
import com.example.safety.model.Email
import kotlinx.coroutines.flow.Flow

class EmailRepository(private val emailDao: EmailDao) {

    val allEmails:Flow<List<Email>> = emailDao.getAll()

    @WorkerThread
    suspend fun insert(email: Email){
        emailDao.insert(email)
    }

    @WorkerThread
    suspend fun deleteById(id:Int){
        emailDao.deleteById(id)
    }
}