package com.example.safety.repository

import androidx.annotation.WorkerThread
import com.example.safety.DAO.PhoneNumberDao
import com.example.safety.model.PhoneNumber
import kotlinx.coroutines.flow.Flow

class PhoneNumberRepository(private val phoneNumberDao: PhoneNumberDao) {
    val allPhoneNumbers:Flow<List<PhoneNumber>> = phoneNumberDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(phoneNumber: PhoneNumber){
        phoneNumberDao.insert(phoneNumber)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteById(id:Int){
        phoneNumberDao.deleteById(id)
    }
}