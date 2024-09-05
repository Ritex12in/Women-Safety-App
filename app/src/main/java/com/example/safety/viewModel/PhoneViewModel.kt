package com.example.safety.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.safety.model.PhoneNumber
import com.example.safety.repository.PhoneNumberRepository
import kotlinx.coroutines.launch

class PhoneViewModel(private val repository: PhoneNumberRepository):ViewModel() {

    val allPhoneNumbers:LiveData<List<PhoneNumber>> = repository.allPhoneNumbers.asLiveData()

    fun insert(phoneNumber: PhoneNumber) = viewModelScope.launch {
        repository.insert(phoneNumber)
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteById(id)
    }
}

class PhoneViewModelFactory(private val repository: PhoneNumberRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhoneViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhoneViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}