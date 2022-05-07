package com.example.kotlinweatherapp.map.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweatherapp.map.view.MapsActivity
import com.example.kotlinweatherapp.models.repo.RepoInterface

class MapViewModelFactory constructor(private val repository: RepoInterface, private var context : Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(this.repository , this.context) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}