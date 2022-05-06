package com.example.kotlinweatherapp.models.pojos

import androidx.room.Entity
import java.io.Serializable

@Entity
class CustomTime(
    var hour : Int ,
    var minute : Int ,
    var amOrPm : String
) : Serializable
