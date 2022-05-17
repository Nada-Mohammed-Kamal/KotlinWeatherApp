package com.example.kotlinweatherapp.homeScreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.models.pojos.WeatherResponse
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DailyWeatherAdapter(var weatherObj: WeatherResponse?, var context: Context): RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var layout = itemView
        var lowTempTextView = itemView.findViewById<TextView>(R.id.txtViewLowTempDailyWeatherRowId)
        var highTempTextView = itemView.findViewById<TextView>(R.id.txtViewHightTempDailyWeatherRowId)
        var dateTimeTextView = itemView.findViewById<TextView>(R.id.txtViewDateAndTimeDailyWeatherRowId)
        var iconImageView = itemView.findViewById<ImageView>(R.id.imgViewIconDailyWeatherRowId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(context)
        var v = inflater.inflate(R.layout.weekly_weather_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.dateTimeTextView.text = timeStampToDate(weatherObj?.daily?.get(position)?.dt?: 0)
        holder.lowTempTextView.text = weatherObj?.daily?.get(position)?.temp?.min.toString()
        holder.highTempTextView.text = weatherObj?.daily?.get(position)?.temp?.max.toString()
        holder.iconImageView.setImageResource(R.drawable.cloudy)
        //leha url call lwa7daha
        Log.e("BASHAAAAAAAAAAAA", "onBindViewHolder:${weatherObj?.daily?.get(position)?.weather?.get(0)?.icon}", )
        Glide.with(holder.iconImageView).load("http://openweathermap.org/img/w/"+weatherObj?.daily?.get(position)?.weather?.get(0)?.icon+".png").into(holder.iconImageView)
    }

    fun timeStampToDate (dt : Long) : String{
        var date : Date = Date(dt * 1000)
        var dateFormat : DateFormat = SimpleDateFormat("MMM d, yyyy")
        return dateFormat.format(date)
    }

    override fun getItemCount() = 7 //3dd ayam al 2asboo3




}