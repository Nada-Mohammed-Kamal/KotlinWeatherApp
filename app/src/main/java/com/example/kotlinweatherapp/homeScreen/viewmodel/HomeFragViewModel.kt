package com.example.kotlinweatherapp.homeScreen.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.example.kotlinweatherapp.models.repo.RepoInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class HomeFragViewModel(private var repoInterface: RepoInterface, private var context: Context) : ViewModel(){
    private var _weatherObjOverNetwork : MutableLiveData<WeatherResponse> = MutableLiveData()
    var weatherObjOverNetwork : LiveData<WeatherResponse> = _weatherObjOverNetwork
     fun getWeatherObj() : LiveData<List<WeatherResponse>>{
            return  repoInterface.getWeatherObjOverNetwork(context)
            Log.e("TAGFromViewModel", "getWeatherObj: $weatherObjOverNetwork", )
    }
}