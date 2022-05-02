package com.example.kotlinweatherapp.homeScreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.R

class HourlyWeatherAdapter(var sports: Weather?, var context: Context): RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>() {

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
        holder.hourTextView.text = sports?.hour
        holder.tempTextView.text = sports?.temp
        holder.hourTempIconImageView.setImageResource(R.drawable.windy)

        //Glide.with(holder.hourTempIconImageView).load(sports?.icon).into(holder.hourTempIconImageView)
    }

    override fun getItemCount() = 24 //b 3dd sa3at al yoom (24 hours)
}