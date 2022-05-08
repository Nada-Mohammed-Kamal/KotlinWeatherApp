package com.example.kotlinweatherapp.models.repo

import android.content.Context
import android.hardware.lights.LightState
import android.location.Location
import androidx.lifecycle.LiveData
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.google.android.gms.maps.model.LatLng
import retrofit2.Response
import java.util.*

interface RepoInterface {
    //mn al network
    fun getWeatherObjOverNetwork(context : Context) : LiveData<List<WeatherResponse>>
    fun getWeatherObjOverNetworkWithLatAndLong(context : Context , latLng: LatLng) : LiveData<List<WeatherResponse>>


        //room

    //vars
    val allStoredWeatherResponses: LiveData<List<WeatherResponse>>
    //val allStoredAlarms : LiveData<List<Alarm>>
    val allStoredAlarms : LiveData<List<Alarm>>

    val allStoredFavourites : LiveData<List<FavouriteObject>>

    //Home Obj
    //fun getAllHomeWeatherObs(): LiveData<List<WeatherResponse>>
    fun insertHomeObj(weatherObj: WeatherResponse?)
    fun deleteHomeWeatherObj(weatherObj: WeatherResponse?)
    fun updateHomeWeatherObj(weatherObj: WeatherResponse?)
    fun getHomeWeatherObj(location: String?): LiveData<WeatherResponse>

    //Alarms
    //fun getAllAlarms(): LiveData<List<Alarm>>
    fun insertAlarm(alarm: Alarm?)
    fun deleteAlarm(alarm: Alarm?)
    fun updateAlarm(alarm: Alarm?)
    fun getAlarmObj(id: UUID?): Alarm?

    fun getAllAlarms() : LiveData<List<Alarm>>

    //Favourites
    //fun getAllFavouriteWeatherObs(): LiveData<List<FavouriteObject>>
    fun insertFavObj(favObj: FavouriteObject?)
    fun deleteFavWeatherObj(favObj: FavouriteObject?)
    fun updateFavWeatherObj(favObj: FavouriteObject?)
    fun getFavWeatherObj(location: Location?): FavouriteObject?
}