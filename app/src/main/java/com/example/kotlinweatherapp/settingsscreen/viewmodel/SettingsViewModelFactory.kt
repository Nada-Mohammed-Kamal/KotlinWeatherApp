package com.example.kotlinweatherapp.settingsscreen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.models.repo.RepoInterface

class SettingsViewModelFactory constructor(private val repository: RepoInterface, private var context : Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsFragViewModel::class.java)) {
            SettingsFragViewModel(this.repository , this.context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}