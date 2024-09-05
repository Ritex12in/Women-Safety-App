package com.example.safety.database

import android.app.Application
import com.example.safety.repository.EmailRepository
import com.example.safety.repository.PhoneNumberRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ContactApplication:Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { ContactRoomDatabase.getDatabase(this,applicationScope) }
    val phoneRepository by lazy { PhoneNumberRepository(database.phoneNumberDao()) }
    val emailRepository by lazy { EmailRepository(database.emailDao()) }

}