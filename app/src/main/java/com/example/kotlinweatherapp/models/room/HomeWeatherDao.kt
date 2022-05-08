package com.example.kotlinweatherapp.models.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kotlinweatherapp.models.pojos.WeatherResponse

@Dao
interface HomeWeatherDao {
    @Query("SELECT * FROM WeatherResponse")
    fun getAllHomeWeatherObs(): LiveData<List<WeatherResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHomeObj(weatherObj: WeatherResponse?)

    @Delete
    fun deleteHomeWeatherObj(weatherObj: WeatherResponse?)

    @Query("DELETE FROM weatherresponse WHERE isFav = 0")
    fun deleteHomeWeatherObjByLatLongId()


    @Update
    fun updateHomeWeatherObj(weatherObj: WeatherResponse?)


    @Query("SELECT * FROM weatherresponse WHERE loc = :location")
    fun getHomeWeatherObj(location: String?): LiveData<WeatherResponse>
}