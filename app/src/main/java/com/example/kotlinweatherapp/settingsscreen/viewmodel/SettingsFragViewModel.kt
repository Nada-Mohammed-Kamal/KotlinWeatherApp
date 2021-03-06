package com.example.kotlinweatherapp.settingsscreen.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.repo.RepoInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragViewModel(private val repository: RepoInterface, private var context : Context) : ViewModel() {
    fun addFavourite(fav : FavouriteObject){
        viewModelScope.launch(Dispatchers.IO){
            Log.e("FromAddFavVM", "$fav", )
            repository.insertFavObj(fav)
        }
    }
}