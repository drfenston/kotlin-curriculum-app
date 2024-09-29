package com.cyrilmaquaire.curriculum.model.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cyrilmaquaire.curriculum.model.StoreData
import com.cyrilmaquaire.curriculum.model.viewmodels.MainViewModel

class MainViewModelFactory(private val userPreferences: StoreData) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

