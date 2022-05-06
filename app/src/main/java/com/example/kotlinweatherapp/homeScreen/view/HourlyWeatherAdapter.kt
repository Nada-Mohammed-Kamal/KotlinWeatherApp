package com.example.kotlinweatherapp.homeScreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HourlyWeatherAdapter(var weatherObj: WeatherResponse?, var context: Context): RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var layout = itemView
        var hourTextView = itemView.findViewById<TextView>(R.id.txtViewHourInHourlyRecViewId)
        var tempTextView = itemView.findViewById<TextView>(R.id.txtViewTempInHourlyRecViewId)
        var hourTempIconImageView = itemView.findViewById<ImageView>(R.id.txtViewimgViewInHourlyRecViewId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(context)
        var v = inflater.inflate(R.layout.hourly_weather_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val timeStampToDate = timeStampToDate(weatherObj?.hourly?.get(position)?.dt?: 0)
        holder.hourTextView.text = formatHours(timeStampToDate)

        holder.tempTextView.text = weatherObj?.hourly?.get(position)?.temp.toString()
        holder.hourTempIconImageView.setImageResource(R.drawable.windy)
        Glide.with(holder.hourTempIconImageView).load("http://openweathermap.org/img/w/"+weatherObj?.hourly?.get(position)?.weather?.get(0)?.icon+".png")
            .apply(
                RequestOptions().override(200,200)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background))
            .into(holder.hourTempIconImageView)



    }

    fun timeStampToDate (dt : Long) : Int{
        var date : Date = Date(dt * 1000)
        return date.hours
    }

    fun formatHours(hour : Int) : String{
        when {
            hour == 0 ->{
                return "12:00 Am"
            }
            hour < 10 -> {
                return "${hour}:00 Am"
            }
            hour in 10..11 -> {
                return "${hour}:00 Am"
            }
            hour == 12 ->{
                return "12:00 Pm"
            }
            hour in 13..23 -> {
                return "${hour-12}:00 pm"
            }
            else -> {
                return ""
            }
        }
    }

    override fun getItemCount() = 24 //b 3dd sa3at al yoom (24 hours)
}