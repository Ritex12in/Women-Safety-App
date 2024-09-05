package com.example.safety.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.safety.model.Email
import com.example.safety.repository.EmailRepository
import kotlinx.coroutines.launch

class EmailViewModel(private val repository: EmailRepository):ViewModel() {

    val allEmails:LiveData<List<Email>> = repository.allEmails.asLiveData()

    fun insert(email: Email) = viewModelScope.launch {
        repository.insert(email)
    }

    fun deleteById(id:Int) = viewModelScope.launch {
        repository.deleteById(id)
    }
}

class EmailViewModelFactory(private val repository: EmailRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}