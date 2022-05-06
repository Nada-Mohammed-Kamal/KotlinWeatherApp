package com.example.kotlinweatherapp.models.pojos
import androidx.room.Entity
import java.io.Serializable

@Entity
data class TempModel (
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
): Serializable