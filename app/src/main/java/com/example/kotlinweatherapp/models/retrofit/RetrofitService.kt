package com.example.kotlinweatherapp.models.retrofit

import androidx.lifecycle.LiveData
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    //https://api.openweathermap.org/data/2.5/onecall?lat=30.560930801547254&lon=31.003141776331283&appid=c22e271e9ebc0d0e0e406902c6b750ee&units=metric&lang=en

    @GET("data/2.5/onecall")
    suspend fun getWeatherObjFromRetrofit(@Query("lat") latitude : String, @Query("lon")longitude : String, @Query("appid") id : String = "c22e271e9ebc0d0e0e406902c6b750ee" , @Query("units")tempUnit : String, @Query("lang")language : String ) : Response<WeatherResponse>

    //@GET("")

     companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}