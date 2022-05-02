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


class DailyWeatherAdapter(var sports: Weather?, var context: Context): RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>() {

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
        holder.dateTimeTextView.text = sports?.dateTime
        holder.lowTempTextView.text = sports?.lowTemp
        holder.highTempTextView.text = sports?.heightTemp
        holder.iconImageView.setImageResource(R.drawable.cloudy)
        //Glide.with(holder.iconImageView).load(sports?.dailyIcon).into(holder.iconImageView)
    }

    override fun getItemCount() = 7//3dd ayam al 2asboo3
}