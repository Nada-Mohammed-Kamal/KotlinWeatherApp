package com.example.kotlinweatherapp.favscreen.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.models.repo.RepoInterface

class FavViewModelFactory constructor(private val repository: RepoInterface, private var context : Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavFragViewModel::class.java)) {
            FavFragViewModel(this.repository , this.context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}