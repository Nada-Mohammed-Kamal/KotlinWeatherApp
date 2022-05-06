package com.example.kotlinweatherapp.models.pojos

import android.location.Location
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FavouriteObject (



    var latitude : Double,
    var longitude : Double,
    var locationName : String

        ){

    @PrimaryKey
    @NonNull
    var locationId : String
    var isFavourite: Boolean = true

    init {
        val loc = "$latitude,$longitude"
        locationId = loc
    }

}