package com.example.kotlinweatherapp.alarmscreen.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweatherapp.R
import com.example.kotlinweatherapp.alarmscreen.AlarmCommunicator
import com.example.kotlinweatherapp.models.pojos.Alarm
import com.example.kotlinweatherapp.models.pojos.WeatherResponse


class AlarmAdapter(var alarms: List<Alarm>, val onPressDelete: (Alarm) -> Unit /*var context: Context*/): RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {
    var myView : View? = null
    lateinit var alarmCommunicator : AlarmCommunicator
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var layout = itemView
        var alarmPlaceTextView = itemView.findViewById<TextView>(R.id.txtViewAlarmPlaceId)
        var deleteAlarmBtn = itemView.findViewById<ImageButton>(R.id.btnDeleteAlarmId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var v = inflater.inflate(R.layout.alarm_row, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        //chamge time zone
        holder.alarmPlaceTextView.text = alarms?.get(position)?.title
        holder.deleteAlarmBtn.setOnClickListener{

            onPressDelete(alarms[position])
        }


        //Glide.with(holder.iconImageView).load(sports?.dailyIcon).into(holder.iconImageView)
    }

    override fun getItemCount() : Int{
        return alarms.size
    }//3dd al alarms al 3ndy ya fal db ya fal obj
}