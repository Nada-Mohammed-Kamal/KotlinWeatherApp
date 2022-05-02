package com.example.kotlinweatherapp.favscreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.model.Weather
import com.example.kotlinweatherapp.R


class FavAdapter(var weather: Weather?, var context: Context): RecyclerView.Adapter<FavAdapter.ViewHolder>() {
    var myView : View? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var layout = itemView
        var alarmPlaceTextView = itemView.findViewById<TextView>(R.id.txtViewFavPlaceId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(context)
        var v = inflater.inflate(R.layout.fav_row, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.alarmPlaceTextView.text = weather?.alarmPlace
        //Glide.with(holder.iconImageView).load(sports?.dailyIcon).into(holder.iconImageView)
    }

    override fun getItemCount() = 4//3dd al alarms al 3ndy ya fal db ya fal obj
}