package com.example.kotlinweatherapp.favscreen.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.repo.RepoInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavFragViewModel(private val repository: RepoInterface, private var context : Context) :
    ViewModel() {

////    private var _listOfAlarms : MutableLiveData<List<Alarm>> = MutableLiveData()
//    private var listOfFavourites : LiveData<List< FavouriteObject>>? = null
//
//    fun getFavourites() : LiveData<List< FavouriteObject>>?{
//        viewModelScope.launch(Dispatchers.IO){
//            //hena al mafrood ykoon _listOfAlarmsss ashoof al moshkala feen
//            listOfFavourites = (repository.allStoredFavourites)
//            Log.e("TAGFromViewModel", "getWeatherObj: $listOfFavourites", )
//        }
//        return listOfFavourites
//    }
//
//    fun deleteFav(fav : FavouriteObject){
//        viewModelScope.launch(Dispatchers.IO){
//            repository.deleteFavWeatherObj(fav)
//            Log.e("FromAlarmViewModel", "deleteFav $fav")
//        }
//    }

    init {
        getFavourites()
    }
    fun getFavourites() : LiveData<List< FavouriteObject>>{
        return repository.allStoredFavourites
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