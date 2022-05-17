package com.example.kotlinweatherapp.favscreen.viewmodel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.repo.RepoInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavFragViewModel(private val repository: RepoInterface, private var context : Context) :
    ViewModel() {

    init {
        getFavourites()
    }


    fun getFavourites() : LiveData<List< FavouriteObject>>{
        //return repository.allStoredFavourites
        return repository.getAllFavouriteWeatherObs()
        //    return repository.allStoredAlarms
    }

    fun deleteFavourite(fav : FavouriteObject){
        viewModelScope.launch(Dispatchers.IO){
            Log.e("FromDeleteFavVM", "$fav", )
            repository.deleteFavWeatherObj(fav)
        }
    }

    fun addFavourite(fav : FavouriteObject){
        viewModelScope.launch(Dispatchers.IO){
            Log.e("FromAddFavVM", "$fav", )
            repository.insertFavObj(fav)
        }
    }
}