package com.example.kotlinweatherapp.models.pojos

import androidx.room.Entity
import java.io.Serializable

@Entity
data class FeelsLikeModel (
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
): Serializable