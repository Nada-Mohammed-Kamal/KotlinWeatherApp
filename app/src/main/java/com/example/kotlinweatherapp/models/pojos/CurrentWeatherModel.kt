package com.example.kotlinweatherapp.models.pojos

import androidx.room.Entity
import java.io.Serializable


data class CurrentWeatherModel (
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<WeatherStatus>,
    val pop: Long? = null
): Serializable