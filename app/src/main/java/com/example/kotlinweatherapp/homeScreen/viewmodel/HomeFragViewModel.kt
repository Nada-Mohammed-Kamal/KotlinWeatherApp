package com.example.kotlinweatherapp.homeScreen.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.example.kotlinweatherapp.models.repo.RepoInterface
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragViewModel(private var repoInterface: RepoInterface, private var context: Context) : ViewModel(){
     fun getWeatherObj() : LiveData<List<WeatherResponse>>{
            return  repoInterface.getWeatherObjOverNetwork(context)
    }


    private var _favWeatherObjOverNetwork : MutableLiveData<WeatherResponse> = MutableLiveData()
    var favWeatherObjOverNetwork : LiveData<WeatherResponse> = _favWeatherObjOverNetwork


    fun getFavWeatherObj(){
            val job = viewModelScope.launch(Dispatchers.IO) {
                try {
                    val oneWeatherObjOverNetworkWithLatAndLong =
                        repoInterface.getOneWeatherObjOverNetworkWithLatAndLong(
                            context,
                            LatLng(
                                SharedPrefsHelper.getLatitude(context).toDouble(),
                                SharedPrefsHelper.getLongitude(context).toDouble()
                            )
                        )
                    _favWeatherObjOverNetwork.postValue(oneWeatherObjOverNetworkWithLatAndLong)

                } catch (e: Exception) {
                    println("erorrrrrrrrrrrrrrrrrrr mat33l3mo ba2a :| ${e.localizedMessage}")
                }

            }
        }


}