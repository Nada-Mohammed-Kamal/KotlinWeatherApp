package com.example.kotlinweatherapp.models.repo

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlinweatherapp.models.networkConnectivity.NetworkChangeReceiver
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.FavouriteObject
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import com.example.kotlinweatherapp.models.retrofit.RemoteSource
import com.example.kotlinweatherapp.models.room.LocalSource
import com.example.kotlinweatherapp.sharedprefs.SharedPrefsHelper
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class Repo private constructor(val remoteSource : RemoteSource, var localSource: LocalSource, var context : Context):
    RepoInterface {

    //vars
    var weatherObjOverNetwork : WeatherResponse? = null

    companion object {
        private var instance : Repo? = null
        fun getInstance(remoteSource : RemoteSource, localSource: LocalSource, context : Context) : Repo {
            return instance ?: Repo(remoteSource , localSource , context)
        }
    }

    override fun getWeatherObjOverNetwork(context: Context): LiveData<List<WeatherResponse>> {
        CoroutineScope(Dispatchers.IO).launch {
            if (NetworkChangeReceiver.isThereInternetConnection) {
                weatherObjOverNetwork = remoteSource.getWeatherObjOverNetwork(context)
                weatherObjOverNetwork?.loc = "${weatherObjOverNetwork?.lat},${weatherObjOverNetwork?.lon}"
                if(!SharedPrefsHelper.getIsFav(context)){
                    //val str = "${SharedPrefsHelper.getLatitude(context)},${SharedPrefsHelper.getLongitude(context)}"
                    localSource.deleteHomeWeatherObjByLatLongId()
                    localSource.insertHomeObj(weatherObjOverNetwork)
                } //fav obj
                  //not in room so should be returned
            }
        }
        if(!SharedPrefsHelper.getIsFav(context)){
            return localSource.allStoredWeatherResponses
        } else {
            val weatherObjOverNetwork1 =  MutableLiveData<List<WeatherResponse>>()
            weatherObjOverNetwork1.value?.plus(weatherObjOverNetwork)
            val o : LiveData<List<WeatherResponse>> = weatherObjOverNetwork1
            //lazm a view hena al obj al rage3 bs
          return o
        }

    }

    override fun getWeatherObjOverNetworkWithLatAndLong(context: Context, latLng: LatLng): LiveData<List<WeatherResponse>> {
        CoroutineScope(Dispatchers.IO).launch {
            if (NetworkChangeReceiver.isThereInternetConnection) {
                weatherObjOverNetwork = remoteSource.getWeatherObjOverNetworkWithLatAndLong(context , latLng)
                weatherObjOverNetwork?.loc = "${weatherObjOverNetwork?.lat},${weatherObjOverNetwork?.lon}"
                if(!(SharedPrefsHelper.getIsFav(context))){
                    localSource.deleteHomeWeatherObjByLatLongId()
                    localSource.insertHomeObj(weatherObjOverNetwork)
                }
            }
        }
        if(!SharedPrefsHelper.getIsFav(context)){
            return localSource.allStoredWeatherResponses
        } else {
            val weatherObjOverNetwork1 =  MutableLiveData<List<WeatherResponse>>()
            weatherObjOverNetwork1.value?.plus(weatherObjOverNetwork)
            val o : LiveData<List<WeatherResponse>> = weatherObjOverNetwork1
            //lazm a view hena al obj al rage3 bs
            return o
        }

    }

    override suspend fun getOneWeatherObjOverNetworkWithLatAndLong(context: Context, latLng: LatLng): WeatherResponse? {
        val weatherObjOverNetwork = remoteSource.getOneWeatherObjOverNetworkWithLatAndLong(context , latLng)
        this.weatherObjOverNetwork = weatherObjOverNetwork
        weatherObjOverNetwork?.loc = "${weatherObjOverNetwork?.lat},${weatherObjOverNetwork?.lon}"
        return weatherObjOverNetwork
    }

    override val allStoredWeatherResponses: LiveData<List<WeatherResponse>>
        get() = localSource.allStoredWeatherResponses
    override val allStoredAlarms: LiveData<List<Alarm>>
        get() = localSource.allStoredAlarms
    override val allStoredFavourites: LiveData<List<FavouriteObject>>
        get() = localSource.allStoredFavourites

    override fun insertHomeObj(weatherObj: WeatherResponse?) {
        localSource.insertHomeObj(weatherObj)
    }

    override fun deleteHomeWeatherObj(weatherObj: WeatherResponse?) {
        localSource.deleteHomeWeatherObj(weatherObj)
    }

    override fun updateHomeWeatherObj(weatherObj: WeatherResponse?) {
        localSource.updateHomeWeatherObj(weatherObj)
    }

    override fun getHomeWeatherObj(location: String?):LiveData<WeatherResponse> {
        return localSource.getHomeWeatherObj(location)
    }

    override fun deleteHomeWeatherObjByLatLongId() {
        localSource.deleteHomeWeatherObjByLatLongId()
    }

    override fun insertAlarm(alarm: Alarm?) {
        localSource.insertAlarm(alarm)
    }

    override fun deleteAlarm(alarm: Alarm?) {
        localSource.deleteAlarm(alarm)
    }

    override fun updateAlarm(alarm: Alarm?) {
        localSource.updateAlarm(alarm)
    }

    override fun getAlarmObj(id: UUID?): Alarm? {
        return  localSource.getAlarmObj(id)

    }

    override fun getAllAlarms(): LiveData<List<Alarm>> {
        return localSource.getAllAlarms()
    }

    override fun getAllFavouriteWeatherObs(): LiveData<List<FavouriteObject>> {
        return localSource.getAllFavouriteWeatherObs()
    }

    override fun insertFavObj(favObj: FavouriteObject?) {
        localSource.insertFavObj(favObj)
    }

    override fun deleteFavWeatherObj(favObj: FavouriteObject?) {
        localSource.deleteFavWeatherObj(favObj)
    }

    override fun updateFavWeatherObj(favObj: FavouriteObject?) {
        localSource.updateFavWeatherObj(favObj)
    }

    override fun getFavWeatherObj(location: Location?): FavouriteObject? {
        return localSource.getFavWeatherObj(location)
    }
}