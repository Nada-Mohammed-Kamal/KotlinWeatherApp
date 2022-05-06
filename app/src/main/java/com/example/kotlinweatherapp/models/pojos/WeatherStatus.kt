package com.example.kotlinweatherapp.models.pojos
import androidx.room.Entity
import java.io.Serializable

@Entity
data class WeatherStatus (
    val id: Long,
    val main: String,
    val description: String,
    val icon: String


): Serializable